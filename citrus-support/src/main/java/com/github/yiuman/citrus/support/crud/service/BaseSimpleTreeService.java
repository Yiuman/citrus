package com.github.yiuman.citrus.support.crud.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.support.crud.query.Query;
import com.github.yiuman.citrus.support.model.BaseTree;
import com.github.yiuman.citrus.support.model.Tree;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 简单树逻辑层
 *
 * @param <E> 实体类型
 * @param <K> 主键类型
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
    public E treeQuery(Query query) {
        return listToTree(list(query));
    }

    protected E listToTree(List<E> list) {
        E root = getRoot();
        listToTree(root, list);
        return root;

    }

    private void listToTree(E current, final List<E> list) {
        Map<K, List<E>> parentIdChildrenMap = list.stream().collect(Collectors.groupingBy(Tree::getParentId));
        list.forEach(entity -> entity.setChildren(parentIdChildrenMap.get(entity.getId()).stream().distinct().collect(Collectors.toList())));
        current.setChildren(list.stream().filter(entity -> Objects.equals(entity.getParentId(), current.getParentId())).collect(Collectors.toList()));
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
        return parentKey == null ? getMapper().selectList(queryWrapper.isNull(getParentField())) : getMapper().selectList(queryWrapper.eq(getParentField(), parentKey));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void move(E current, K moveTo) throws Exception {
        current.setParentId(moveTo);
        save(current);
    }

    @Override
    public List<E> children(E current) {
        return loadByParent(current.getId());
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
