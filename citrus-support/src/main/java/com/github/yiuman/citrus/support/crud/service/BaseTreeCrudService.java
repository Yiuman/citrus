package com.github.yiuman.citrus.support.crud.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.github.yiuman.citrus.support.crud.mapper.TreeMapper;
import com.github.yiuman.citrus.support.model.Tree;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 预排序遍历树形结构的Crud
 *
 * @author yiuman
 * @date 2020/4/7
 */
public abstract class BaseTreeCrudService<M extends TreeMapper<E>, E extends Tree<K>, K>
        extends BaseCrudService<M, E, K>
        implements TreeService<E, K> {

    private final static String UPDATE_ADD_FORMAT = "%s=%s+%s";

    private final static String UPDATE_REDUCTION_FORMAT = "%s=%s-%s";

    @Override
    public E getRoot() {
        return getBaseMapper().selectOne(new QueryWrapper<E>().isNull(getParentField()));
    }

    @Override
    public synchronized void reInit() throws Exception {
        reInit(getRoot());
    }

    protected void reInit(E current) throws Exception {
        saveEntity(current);
        List<E> children = loadByParent(current.getId());
        if (!CollectionUtils.isEmpty(children)) {
            children.forEach(LambdaUtils.consumerWrapper(this::reInit));
        }
    }

    @Override
    public E load(boolean isLazy) {
        E current = getRoot();
        load(current, isLazy);
        return current;
    }

    @Override
    public void load(E current) {
        load(current, true);
    }

    @Override
    public void load(E current, boolean isLazy) {
        if (isLazy) {
            current.setChildren(loadByParent(current.getId()));
        } else {
            List<E> children = children(current, current.getDeep() + 1);
            if (!CollectionUtils.isEmpty(children)) {
                children.parallelStream().forEach(this::load);
            }
            current.setChildren(children);
        }
    }


    @Override
    public void beforeSave(E entity) throws Exception {
        this.insert(entity);
    }

    @Override
    public void insert(E current) throws Exception {
        //1.获取当前父节点
        E parent = Optional
                .ofNullable(get(current.getParentId()))
                .orElse(getRoot());
        //2.更新所有左值大于当前父节点左值的节点左值 +2
        //3.更新所有右值大于当前父节点右值的节点右值 +2
        int rightValue = 1;
        int deep = 1;
        //4.更新父节点
        if (parent != null && current.getId() != parent.getId()) {
            beforeSaveOrUpdate(parent);
            rightValue = parent.getRightValue();
            deep = parent.getDeep() + 1;
            parent.setRightValue(rightValue + 2);
            saveOrUpdate(parent);
            current.setParentId(parent.getId());
        } else {
            current.setParentId(null);
        }

        //5.设置当前节点的左右值
        current.setLeftValue(rightValue);
        current.setRightValue(rightValue + 1);
        current.setDeep(deep);
        //Over
    }

    @Override
    public void move(E current, K moveTo) throws Exception {
        //更新树
        beforeDeleteOrMove(current);

        //变更父ID
        current.setParentId(moveTo);
        saveEntity(current);

        List<E> children = children(current);
        //子节点不为空时重新生成左右值
        if (!CollectionUtils.isEmpty(children)) {
            children.forEach(LambdaUtils.consumerWrapper(this::saveEntity));
        }
    }

    /**
     * 重写普通CRUD的删除方法，在删除前做更新节点操作
     *
     * @param key 当前删除的主键
     */
    @SuppressWarnings("unchecked")
    @Override
    public void delete(K key) throws Exception {
        E currentEntity = get(key);
        List<K> collect = children(currentEntity).parallelStream().map(Tree::getId).collect(Collectors.toList());
        removeByIds((Collection<? extends Serializable>) collect);
        delete(currentEntity);
        super.delete(key);
    }

    @Override
    public void delete(E current) {
        beforeDeleteOrMove(current);
    }

    @Override
    public List<E> loadByParent(K parentKey) {
        QueryWrapper<E> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(getParentField(), parentKey);
        return list(queryWrapper);
    }

    @Override
    public List<E> children(E current) {
        // target.left > this.left  and target.right < this.right
        return list(new QueryWrapper<E>()
                .gt(getLeftField(), current.getLeftValue())
                .lt(getRightField(), current.getRightValue()));
    }

    @Override
    public List<E> children(E current, int deep) {
        return list(new QueryWrapper<E>()
                .gt(getLeftField(), current.getLeftValue())
                .lt(getRightField(), current.getRightValue())
                .eq(getDeepField(), deep));
    }

    @Override
    public List<E> parents(E current) {
        return list(new QueryWrapper<E>()
                .gt(getRightField(), current.getRightValue())
                .le(getLeftField(), current.getLeftValue()));
    }

    @Override
    public List<E> parents(E current, int high) {
        return list(new QueryWrapper<E>()
                .gt(getRightField(), current.getRightValue())
                .le(getLeftField(), current.getLeftValue())
                .eq(getDeepField(), high));
    }

    @Override
    public List<E> siblings(E current) {
        return list(new QueryWrapper<E>().eq(getParentField(), current.getParentId()));
    }


    /**
     * 当前节点保存前的做的操作
     *
     * @param parent 当前节点的父节点
     */
    private void beforeSaveOrUpdate(E parent) {
        //2.更新所有左值大于当前父节点右值的节点左值 +2
        update(new UpdateWrapper<E>()
                .setSql(String.format(UPDATE_ADD_FORMAT, getLeftField(), getLeftField(), 2))
                .gt(getLeftField(), parent.getRightValue()));

        //3.更新所有右值大于当前父节点右值的节点右值 +2
        update(new UpdateWrapper<E>()
                .setSql(String.format(UPDATE_ADD_FORMAT, getRightField(), getRightField(), 2))
                .gt(getRightField(), parent.getRightValue()));
    }

    /**
     * 删除或移动前做的操作
     *
     * @param current 当前节点
     */
    private void beforeDeleteOrMove(E current) {
        //1.更新所有右值小于当前父节点右值的节点左值 -2
        update(new UpdateWrapper<E>()
                .setSql(String.format(UPDATE_REDUCTION_FORMAT, getLeftField(), getLeftField(), 2))
                .gt(getLeftField(), current.getRightValue()));

        //2.更新所有右值小于当前父节点右值的节点右值 +2
        update(new UpdateWrapper<E>()
                .setSql(String.format(UPDATE_REDUCTION_FORMAT, getRightField(), getRightField(), 2))
                .gt(getRightField(), current.getRightValue()));
    }

    @Override
    public E treeQuery(Wrapper<E> wrapper) {
        if (wrapper == null) {
            return load(false);
        }
        //查询符合条件的列表
        List<E> list = list(wrapper);
        //找到列表ID
        List<K> ids = list.parallelStream().map(Tree::getId).collect(Collectors.toList());
        TableInfo table = SqlHelper.table(entityClass);

        //将查询到的列表的项的所有父节点查出来
        String parentSql = "t1.leftValue > t2. leftValue and t1.rightValue < t2.rightValue"
                .replaceAll("leftValue",getLeftField())
                .replaceAll("rightValue",getRightField());
        list.addAll(getBaseMapper().list(table.getTableName(), new QueryWrapper<E>().apply(parentSql)
                .in("t1." + table.getKeyColumn(), ids)));
        E root = getRoot();
        //传list进去前需要去重
        initTreeFromList(root, list.parallelStream().distinct().collect(Collectors.toList()));
        return root;
    }

    protected void initTreeFromList(E start, List<E> list) {
        List<E> childrenOfStart = list.parallelStream()
                .filter(current -> current.getParentId() != null && current.getParentId().equals(start.getId())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(childrenOfStart)) {
            start.setChildren(childrenOfStart);
            childrenOfStart.parallelStream().forEach(next -> initTreeFromList(next, list));
        }
    }

}
