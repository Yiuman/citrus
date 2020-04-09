package com.github.yiuman.citrus.support.model;

import java.util.List;

/**
 * 树形结构，左右值算法
 *
 * @author yiuman
 * @date 2020/4/7
 */
public interface Tree<K> {

    K getId();

    K getParentId();

    void setParentId(K parentId);

    Integer getLeftValue();

    void setLeftValue(Integer leftValue);

    Integer getRightValue();

    void setRightValue(Integer rightValue);

    boolean isLeaf();

    Integer getDeep();

    void setDeep(Integer deep);

    List<? extends Tree<K>> getChildren();

    void setChildren(List<? extends Tree<K>> children);

}