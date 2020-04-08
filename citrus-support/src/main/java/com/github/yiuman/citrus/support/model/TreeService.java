package com.github.yiuman.citrus.support.model;

import java.util.List;

/**
 * 属性操作逻辑类
 *
 * @author yiuman
 * @date 2020/4/7
 */
public interface TreeService<T extends Tree<K>, K> {

    /**
     * 根节点
     */
    T getRoot();

    default String getDeepField(){
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
     * 插入当前节点
     */
    void insert(T current) throws Exception;

    /**
     * 移动
     */
    void move(T current,K moveTo) throws Exception;

    /**
     * 删除
     */
    void delete(T current) throws Exception;

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