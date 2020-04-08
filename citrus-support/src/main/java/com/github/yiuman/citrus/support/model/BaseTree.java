package com.github.yiuman.citrus.support.model;


import com.baomidou.mybatisplus.annotation.TableField;

import java.util.List;

/**
 * 树形结构，左右值算法
 *
 * @author yiuman
 * @date 2020/3/30
 */
public abstract class BaseTree<T extends Tree<K>, K> implements Tree<K> {

    private Integer leftValue;

    private Integer rightValue;

    @TableField(exist = false)
    private List<T> children;

    public BaseTree() {
    }

    public Integer getLeftValue() {
        return leftValue;
    }

    public void setLeftValue(Integer leftValue) {
        this.leftValue = leftValue;
    }

    public Integer getRightValue() {
        return rightValue;
    }

    public void setRightValue(Integer rightValue) {
        this.rightValue = rightValue;
    }

    public boolean isLeaf() {
        return this.getLeftValue() != null && this.getRightValue() != null && this.getRightValue() - this.getLeftValue() == 1;
    }

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }
}
