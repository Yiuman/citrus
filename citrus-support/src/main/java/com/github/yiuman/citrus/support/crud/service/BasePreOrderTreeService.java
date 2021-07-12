package com.github.yiuman.citrus.support.crud.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.github.yiuman.citrus.support.crud.CrudHelper;
import com.github.yiuman.citrus.support.crud.mapper.TreeMapper;
import com.github.yiuman.citrus.support.model.BasePreOrderTree;
import com.github.yiuman.citrus.support.model.Tree;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 左右值预遍历树逻辑层
 *
 * @author yiuman
 * @date 2020/4/15
 */
public abstract class BasePreOrderTreeService<E extends BasePreOrderTree<E, K>, K extends Serializable> implements TreeCrudService<E, K> {

    protected static final Logger log = LoggerFactory.getLogger(BasePreOrderTreeService.class);

    private final static String UPDATE_ADD_FORMAT = "%s=%s+%s";

    private final static String UPDATE_REDUCTION_FORMAT = "%s=%s-%s";


    protected BaseService<E, K> getService() {
        return CrudHelper.getCrudService(getClass());
    }

    protected TreeMapper<E> getTreeMapper() {
        return CrudHelper.getTreeMapper(getEntityType());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean beforeSave(E entity) throws Exception {
        if (entity.getId() != null) {
            return true;
        }
        //1.获取当前父节点
        E parent = Optional
                .ofNullable(get(entity.getParentId()))
                .orElse(getRoot());
        int rightValue = 1;
        int deep = 1;
        //4.更新父节点
        if (parent != null && entity.getId() != parent.getId()) {
            beforeSaveOrUpdate(parent);
            rightValue = parent.getRightValue();
            deep = parent.getDeep() + 1;
            parent.setRightValue(rightValue + 2);
            getService().save(parent);
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public K save(E entity) throws Exception {
        if (!this.beforeSave(entity)) {
            return null;
        }
        return getService().save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean batchSave(Iterable<E> entityIterable) {
        return getService().batchSave(entityIterable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean beforeRemove(E entity) {
        //1.更新所有右值小于当前父节点右值的节点左值 -2
        this.beforeDeleteOrMove(entity);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean remove(E entity) {
        if (!this.beforeRemove(entity)) {
            return false;
        }

        return getService().remove(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean remove(Wrapper<E> wrappers) {
        return getService().remove(wrappers);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchRemove(Iterable<K> keys) {
        List<K> keyList = new ArrayList<>();
        keys.forEach(keyList::add);
        getTreeMapper().deleteBatch(list(Wrappers.<E>query().in(getKeyColumn(), keyList)));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void clear() {
        getService().clear();
    }

    @Override
    public E get(K key) {
        return getService().get(key);
    }

    @Override
    public E get(Wrapper<E> wrapper) {
        return getService().get(wrapper);
    }

    @Override
    public List<E> list() {
        return getService().list();
    }

    @Override
    public List<E> list(Wrapper<E> wrapper) {
        return getService().list(wrapper);
    }

    @Override
    public <P extends IPage<E>> P page(P page, Wrapper<E> queryWrapper) {
        return getService().page(page, queryWrapper);
    }

    @Override
    public E getRoot() {
        return getTreeMapper().selectOne(Wrappers.<E>query().isNull(getParentField()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized void reInit() throws Exception {
        reInit(getRoot());
    }

    @Transactional(rollbackFor = Exception.class)
    protected void reInit(E current) throws Exception {
        getService().save(current);
        List<E> children = loadByParent(current.getId());
        if (!CollectionUtils.isEmpty(children)) {
            children.forEach(LambdaUtils.consumerWrapper(this::reInit));
        }
    }

    @Override
    public E load(boolean isLazy) {
        E current = getRoot();
        if (current == null) {
            return null;
        }
        load(current, isLazy);
        return current;
    }

    @Override
    public E treeQuery(Wrapper<E> wrapper) {
        if (wrapper == null) {
            return load(false);
        }
        //查询符合条件的列表
        List<E> list = list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        //找到列表ID
        List<K> ids = list.parallelStream().map(Tree::getId).collect(Collectors.toList());
        TableInfo table = SqlHelper.table(getService().getEntityType());

        //将查询到的列表的项的所有父节点查出来
        String parentSql = "t1.leftValue > t2. leftValue and t1.rightValue < t2.rightValue"
                .replaceAll("leftValue", getLeftField())
                .replaceAll("rightValue", getRightField());
        list.addAll(getTreeMapper().treeLink(table.getTableName(), Wrappers.<E>query().apply(parentSql).in("t1." + table.getKeyColumn(), ids)));
        final E root = getRoot();
        //传list进去前需要去重,并排除根节点
        initTreeFromList(root, list.parallelStream().distinct().filter(item -> item != null && item.getId() != root.getId()).collect(Collectors.toList()));
        return root;
    }

    /**
     * 此处记得去重，实体记得重写equals&hasCode
     *
     * @param start 挂载的节点
     * @param list  节点列表
     */
    protected void initTreeFromList(E start, List<E> list) {
        List<E> childrenOfStart = list.parallelStream()
                .filter(current -> current.getParentId() != null && current.getParentId().equals(start.getId())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(childrenOfStart)) {
            start.setChildren(childrenOfStart);
            childrenOfStart.parallelStream().forEach(next -> initTreeFromList(next, list));
        }
    }

    @Override
    public void load(E current) {
        load(current, true);
    }

    @Override
    public void load(E current, boolean isLazy) {
        if (isLazy) {
            List<E> children = loadByParent(current.getId());
            children.parallelStream().forEach(childNode -> {
                if (!childNode.isLeaf()) {
                    childNode.setChildren(new ArrayList<>());
                }
            });
            current.setChildren(children);
        } else {
            initTreeFromList(current, list());
        }
    }

    @Override
    public List<E> loadByParent(K parentKey) {
        return list(Wrappers.<E>query().eq(getParentField(), parentKey));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void move(E current, K moveTo) throws Exception {
        //更新树
        this.beforeDeleteOrMove(current);
        //变更父ID
        current.setParentId(moveTo);
        getService().save(current);

        List<E> children = children(current);
        //子节点不为空时重新生成左右值
        if (!CollectionUtils.isEmpty(children)) {
            children.forEach(LambdaUtils.consumerWrapper(this::save));
        }
    }

    @Override
    public List<E> children(E current) {
        // target.left > this.left  and target.right < this.right
        return list(Wrappers.<E>query()
                .gt(getLeftField(), current.getLeftValue())
                .lt(getRightField(), current.getRightValue()));
    }

    @Override
    public List<E> children(E current, int deep) {
        return list(Wrappers.<E>query()
                .gt(getLeftField(), current.getLeftValue())
                .lt(getRightField(), current.getRightValue())
                .eq(getDeepField(), deep));
    }

    @Override
    public List<E> parents(E current) {
        return list(Wrappers.<E>query()
                .gt(getRightField(), current.getRightValue())
                .le(getLeftField(), current.getLeftValue()));
    }

    @Override
    public E parent(E current, int high) {
        return getTreeMapper().selectOne(Wrappers.<E>query()
                .gt(getRightField(), current.getRightValue())
                .le(getLeftField(), current.getLeftValue())
                .eq(getDeepField(), high));
    }

    @Override
    public List<E> siblings(E current) {
        return list(Wrappers.<E>query().eq(getParentField(), current.getParentId()));
    }

    /**
     * 当前节点保存前的做的操作
     *
     * @param parent 当前节点的父节点
     */
    private void beforeSaveOrUpdate(E parent) {

        //2.更新所有左值大于当前父节点右值的节点左值 +2
        getTreeMapper().update(null, Wrappers.<E>update()
                .setSql(String.format(UPDATE_ADD_FORMAT, getLeftField(), getLeftField(), 2))
                .gt(getLeftField(), parent.getRightValue()));

        //3.更新所有右值大于当前父节点右值的节点右值 +2
        getTreeMapper().update(null, Wrappers.<E>update()
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
        getTreeMapper().update(null, Wrappers.<E>update()
                .setSql(String.format(UPDATE_REDUCTION_FORMAT, getLeftField(), getLeftField(), 2))
                .gt(getLeftField(), current.getRightValue()));

        //2.更新所有右值小于当前父节点右值的节点右值 +2
        getTreeMapper().update(null, Wrappers.<E>update()
                .setSql(String.format(UPDATE_REDUCTION_FORMAT, getRightField(), getRightField(), 2))
                .gt(getRightField(), current.getRightValue()));
    }

}
