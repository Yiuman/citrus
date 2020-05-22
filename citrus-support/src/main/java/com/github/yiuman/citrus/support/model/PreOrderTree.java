package com.github.yiuman.citrus.support.model;

/**
 * 预遍历排序树，左右值算法
 * @author yiuman
 * @date 2020/5/22
 */
public interface PreOrderTree<K> extends Tree<K> {

    Integer getLeftValue();

    void setLeftValue(Integer leftValue);

    Integer getRightValue();

    void setRightValue(Integer rightValue);

    Integer getDeep();

    void setDeep(Integer deep);
}