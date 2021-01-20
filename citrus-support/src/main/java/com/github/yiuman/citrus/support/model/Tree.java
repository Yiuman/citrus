package com.github.yiuman.citrus.support.model;

import java.util.List;

/**
 * 树形结构
 *
 * @author yiuman
 * @date 2020/4/7
 */
public interface Tree<K> {

    /**
     * 获取节点的主键ID
     *
     * @return ID
     */
    K getId();

    /**
     * 获取父节点的主键ID
     *
     * @return ID
     */
    K getParentId();

    /**
     * 设置父节点ID
     *
     * @param parentId 父节点主键ID
     */
    void setParentId(K parentId);

    /**
     * 判断是否为叶子节点
     *
     * @return true/false
     */
    boolean isLeaf();

    /**
     * 获取当前节点的子孙节点
     *
     * @return 子孙节点的结合
     */
    List<? extends Tree<K>> getChildren();

}