package com.github.yiuman.citrus.support.crud.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yiuman.citrus.support.model.BasePreOrderTree;
import com.github.yiuman.citrus.support.model.BaseTree;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 简单树逻辑层
 *
 * @author yiuman
 * @date 2020/5/22
 */
public abstract class BaseSimpleTreeService<E extends BaseTree<E, K>, K extends Serializable>
        extends BaseService<E, K>
        implements TreeCrudService<E, K> {

    @Override
    public E getRoot() {
        //创建虚拟节点
        return BeanUtils.instantiateClass(getEntityType());
    }

    @Override
    public void reInit() throws Exception {
    }

    @Override
    public E load(boolean isLazy) throws Exception {
        E root = getRoot();
        load(root, isLazy);
        return root;
    }

    @Override
    public E treeQuery(Wrapper<E> wrapper) throws Exception {
        return initSimpleTreeByList(list(wrapper));
    }

    protected E initSimpleTreeByList(List<E> list){
        E root = getRoot();
        final Set<E> existsEntity= new LinkedHashSet<>(list);
        list.parallelStream().forEach(item-> existsEntity.addAll(parents(item)));
        this.mountByList(root,existsEntity);
        return root;

    }

    private void mountByList(E current,final Set<E> sets){
        List<E> children = sets.parallelStream().filter(item -> item.getParentId().equals(current.getId())).collect(Collectors.toList());
        current.setChildren(children);
        children.parallelStream().forEach(item->mountByList(item,sets));
    }

    @Override
    public void load(E current) throws Exception {
        load(true);
    }

    @Override
    public void load(E current, boolean isLazy) throws Exception {
        List<E> children = loadByParent(current.getParentId());
        current.setChildren(children);
        if (!isLazy) {
            children.forEach(LambdaUtils.consumerWrapper(item -> load(item, false)));
        }
    }

    @Override
    public List<E> loadByParent(K parentKey) {
        QueryWrapper<E> queryWrapper = new QueryWrapper<>();
        return parentKey==null?list(queryWrapper.isNull(getParentField())):list(queryWrapper.eq(getParentField(), parentKey));
    }

    @Override
    public void move(E current, K moveTo) throws Exception {
        current.setParentId(moveTo);
        save(current);
    }

    @Override
    public List<E> children(E current) {
        return loadByParent(current.getParentId());
    }

    /**
     * 简单树无法区分深度
     */
    @Override
    public List<E> children(E current, int deep) {
        return children(current);
    }

    @Override
    public List<E> parents(E current) {
        List<E> parents = new ArrayList<>();
        while (current.getParentId() != null) {
            parents.add(get(current.getParentId()));
        }

        return parents;
    }

    @Override
    public List<E> parents(E current, int high) {
        return parents(current);
    }

    @Override
    public List<E> siblings(E current) {
        return loadByParent(current.getParentId());
    }
}
