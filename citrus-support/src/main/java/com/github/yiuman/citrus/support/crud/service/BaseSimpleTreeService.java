package com.github.yiuman.citrus.support.crud.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.support.model.BaseTree;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
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
    public void reInit() {
    }

    @Override
    public E load(boolean isLazy) {
        E root = getRoot();
        load(root, isLazy);
        return root;
    }

    @Override
    public E treeQuery(Wrapper<E> wrapper) {
        return initSimpleTreeByList(list(wrapper));
    }

    protected E initSimpleTreeByList(List<E> list) {
        E root = getRoot();
        final Set<E> existsEntity = Collections.synchronizedSet(new LinkedHashSet<>(list));
        list.parallelStream().forEach(item -> existsEntity.addAll(parents(item)));
        this.mountByList(root, existsEntity);
        return root;

    }

    private void mountByList(E current, final Set<E> sets) {
        //这里的根节点肯定是虚拟节点，ID为空，第二级挂的父节点ID为空，所以需要这么判断
        List<E> children = sets.stream().filter(item -> (
                item.getId() != null
                        && !item.getId().equals(current.getId())
                        && (current.getId() == item.getParentId() || (item.getParentId() != null && item.getParentId().equals(current.getId()))
                ))).collect(Collectors.toList());
        current.setChildren(children);
        children.parallelStream().forEach(item -> mountByList(item, sets));
    }

    @Override
    public void load(E current) {
        load(true);
    }

    @Override
    public void load(E current, boolean isLazy) {
        List<E> children = loadByParent(current.getId());
        current.setChildren(children);
        if (!isLazy) {
            children.forEach(LambdaUtils.consumerWrapper(item -> load(item, false)));
        }
    }

    @Override
    public List<E> loadByParent(K parentKey) {
        QueryWrapper<E> queryWrapper = Wrappers.query();
        return parentKey == null ? list(queryWrapper.isNull(getParentField())) : list(queryWrapper.eq(getParentField(), parentKey));
    }

    @Transactional(rollbackFor = Exception.class)
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
            E parent = get(current.getParentId());
            parents.add(parent);
            current = parent;
        }

        return parents;
    }

    @Override
    public E parent(E current, int high) {
        E parent = current;
        while (high >= 0) {
            if (parent == null || current.getParentId() == null) {
                break;
            }
            parent = get(current.getParentId());
            high--;
        }
        return parent;
    }

    @Override
    public List<E> siblings(E current) {
        return loadByParent(current.getParentId());
    }
}
