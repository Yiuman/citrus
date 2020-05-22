package com.github.yiuman.citrus.support.model;

import java.util.List;

/**
 * 树形结构
 *
 * @author yiuman
 * @date 2020/4/7
 */
public interface Tree<K> {

    K getId();

    K getParentId();

    void setParentId(K parentId);

    boolean isLeaf();

    List<? extends Tree<K>> getChildren();


}