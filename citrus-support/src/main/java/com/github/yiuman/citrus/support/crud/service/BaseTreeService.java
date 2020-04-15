package com.github.yiuman.citrus.support.crud.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.github.yiuman.citrus.support.crud.mapper.TreeMapper;
import com.github.yiuman.citrus.support.model.Tree;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yiuman
 * @date 2020/4/15
 */
public abstract class BaseTreeService<E extends Tree<K>, K extends Serializable> implements TreeCrudService<E, K> {

    private final static String UPDATE_ADD_FORMAT = "%s=%s+%s";

    private final static String UPDATE_REDUCTION_FORMAT = "%s=%s-%s";

    private final BaseService<E, K> ekBaseService = new BaseService<E, K>() {
        @Override
        protected BaseMapper<E> getMapper() {
            return getTreeMapper();
        }

        @Override
        public Class<E> getEntityType() {
            return BaseTreeService.this.getEntityType();
        }

        @Override
        public Class<K> getKeyType() {
            return BaseTreeService.this.getKeyType();
        }
    };

    protected abstract TreeMapper<E> getTreeMapper();

    @Override
    public boolean beforeSave(E entity) throws Exception {
        //1.获取当前父节点
        E parent = Optional
                .ofNullable(get(entity.getParentId()))
                .orElse(getRoot());
        //2.更新所有左值大于当前父节点左值的节点左值 +2
        //3.更新所有右值大于当前父节点右值的节点右值 +2
        int rightValue = 1;
        int deep = 1;
        //4.更新父节点
        if (parent != null && entity.getId() != parent.getId()) {
            ekBaseService.save(entity);
            rightValue = parent.getRightValue();
            deep = parent.getDeep() + 1;
            parent.setRightValue(rightValue + 2);
            ekBaseService.save(parent);
            entity.setParentId(parent.getId());
        } else {
            entity.setParentId(null);
        }

        //5.设置当前节点的左右值
        entity.setLeftValue(rightValue);
        entity.setRightValue(rightValue + 1);
        entity.setDeep(deep);
        return true;
        //Over
    }

    @Override
    public K save(E entity) throws Exception {
        if (!this.beforeSave(entity)) {
            return null;
        }
        return ekBaseService.save(entity);
    }

    @Override
    public boolean batchSave(Iterable<E> entityIterable) {
        return ekBaseService.batchSave(entityIterable);
    }

    @Override
    public boolean beforeRemove(E entity) {
        //1.更新所有右值小于当前父节点右值的节点左值 -2
        getTreeMapper().update(null, new UpdateWrapper<E>()
                .setSql(String.format(UPDATE_ADD_FORMAT, getLeftField(), getLeftField(), 2))
                .gt(getLeftField(), entity.getRightValue()));

        //2.更新所有右值小于当前父节点右值的节点右值 +2
        getTreeMapper().update(null, new UpdateWrapper<E>()
                .setSql(String.format(UPDATE_REDUCTION_FORMAT, getRightField(), getRightField(), 2))
                .gt(getRightField(), entity.getRightValue()));
        return true;
    }

    @Override
    public boolean remove(E entity) throws Exception {
        if (!this.beforeRemove(entity)) {
            return false;
        }
        return ekBaseService.remove(entity);
    }

    @Override
    public void batchRemove(Iterable<K> keys) {
        ekBaseService.batchRemove(keys);
    }

    @Override
    public void clear() {
        ekBaseService.clear();
    }

    @Override
    public E get(K key) {
        return ekBaseService.get(key);
    }

    @Override
    public List<E> list() {
        return ekBaseService.list();
    }

    @Override
    public List<E> list(Wrapper<E> wrapper) {
        return ekBaseService.list(wrapper);
    }

    @Override
    public <P extends IPage<E>> P page(P page, Wrapper<E> queryWrapper) {
        return ekBaseService.page(page, queryWrapper);
    }

    @Override
    public E getRoot() {
        return getTreeMapper().selectOne(new QueryWrapper<E>().isNull(getParentField()));
    }

    @Override
    public synchronized void reInit() throws Exception {
        reInit(getRoot());
    }

    protected void reInit(E current) throws Exception {
        ekBaseService.save(current);
        List<E> children = loadByParent(current.getId());
        if (!CollectionUtils.isEmpty(children)) {
            children.forEach(LambdaUtils.consumerWrapper(this::reInit));
        }
    }

    @Override
    public E load(boolean isLazy) throws Exception {
        E current = getRoot();
        load(current, isLazy);
        return current;
    }

    @Override
    public E treeQuery(Wrapper<E> wrapper) throws Exception {
        if (wrapper == null) {
            return load(false);
        }
        //查询符合条件的列表
        List<E> list = list(wrapper);
        //找到列表ID
        List<K> ids = list.parallelStream().map(Tree::getId).collect(Collectors.toList());
        TableInfo table = SqlHelper.table(ekBaseService.getEntityType());

        //将查询到的列表的项的所有父节点查出来
        String parentSql = "t1.leftValue > t2. leftValue and t1.rightValue < t2.rightValue"
                .replaceAll("leftValue", getLeftField())
                .replaceAll("rightValue", getRightField());
        list.addAll(getTreeMapper().list(table.getTableName(), new QueryWrapper<E>().apply(parentSql)
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

    @Override
    public void load(E current) throws Exception {
        load(current, true);
    }

    @Override
    public void load(E current, boolean isLazy) throws Exception {
        if (isLazy) {
            current.setChildren(loadByParent(current.getId()));
        } else {
            List<E> children = children(current, current.getDeep() + 1);
            if (!CollectionUtils.isEmpty(children)) {
                children.parallelStream().forEach(LambdaUtils.consumerWrapper(this::load));
            }
            current.setChildren(children);
        }
    }

    @Override
    public List<E> loadByParent(K parentKey) {
        return list(new QueryWrapper<E>().eq(getParentField(), parentKey));
    }

    @Override
    public void move(E current, K moveTo) throws Exception {
        //更新树
        beforeRemove(current);

        //变更父ID
        current.setParentId(moveTo);
        ekBaseService.save(current);

        List<E> children = children(current);
        //子节点不为空时重新生成左右值
        if (!CollectionUtils.isEmpty(children)) {
            children.forEach(LambdaUtils.consumerWrapper(this::save));
        }
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
}
