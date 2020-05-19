package com.github.yiuman.citrus.support.crud.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.github.yiuman.citrus.support.model.Tree;

import java.io.Serializable;
import java.util.List;

/**
 * 属性操作接口
 *
 * @author yiuman
 * @date 2020/4/15
 */
public interface TreeOperation<T extends Tree<K>, K extends Serializable> {

    /**
     * 根节点
     */
    T getRoot();

    default String getDeepField() {
        return "deep";
    }

    default String getParentField() {
        return "parent_id";
    }

    default String getLeftField() {
        return "left_value";
    }

    default String getRightField() {
        return "right_value";
    }

    /**
     * 从新构建预遍历树
     */
    void reInit() throws Exception;

    /**
     * 加载树
     *
     * @param isLazy 是否懒加载
     * @return 树
     */
    T load(boolean isLazy) throws Exception;

    /**
     * 树查询
     *
     * @param wrapper 查询Wrapper
     * @return 树结构
     */
    T treeQuery(Wrapper<T> wrapper) throws Exception;

    /**
     * 加载某个节点下所有数据
     *
     * @param current 当前节点
     */
    void load(T current) throws Exception;

    /**
     * 加载某个节点的下级节点
     *
     * @param current 当前节点
     * @param isLazy  是否懒加载
     */
    void load(T current, boolean isLazy) throws Exception;

    /**
     * 根据父节点ID加载子列表
     *
     * @param parentKey 父节点ID
     * @return 子列表
     */
    List<T> loadByParent(K parentKey);

    /**
     * 移动
     */
    void move(T current, K moveTo) throws Exception;

    /**
     * 查询某个节点的后代
     */
    List<T> children(T current);

    /**
     * 根据深度查询某个节点后代
     *
     * @param current 当前节点
     * @param deep    查询的深度
     * @return 子节点
     */
    List<T> children(T current, int deep);

    /**
     * 查询所有祖先节点
     *
     * @param current 当前节点
     * @return 祖先节点
     */
    List<T> parents(T current);

    /**
     * 查询对应高度的祖先节点
     *
     * @param current 当前节点
     * @param high    祖先节点的高度
     * @return 祖先节点
     */
    List<T> parents(T current, int high);

    /**
     * 查询所有兄弟节点
     *
     * @param current 当前节点
     * @return 兄弟节点
     */
    List<T> siblings(T current);

}