package com.github.yiuman.citrus.support.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

/**
 * 树形结构，左右值算法
 *
 * @author yiuman
 * @date 2020/3/30
 */
public abstract class BaseTree<T extends BaseTree<T, K>, K> implements Tree<K> {

    private Integer leftValue;

    private Integer rightValue;

    private Integer deep;

    @JsonDeserialize
    @TableField(exist = false)
    private List<T> children;

    public BaseTree() {
    }

    @Override
    public Integer getLeftValue() {
        return leftValue;
    }

    @Override
    public void setLeftValue(Integer leftValue) {
        this.leftValue = leftValue;
    }

    @Override
    public Integer getRightValue() {
        return rightValue;
    }

    @Override
    public void setRightValue(Integer rightValue) {
        this.rightValue = rightValue;
    }

    @Override
    public boolean isLeaf() {
        return this.getLeftValue() != null && this.getRightValue() != null && this.getRightValue() - this.getLeftValue() == 1;
    }

    @Override
    public Integer getDeep() {
        return deep;
    }

    @Override
    public void setDeep(Integer deep) {
        this.deep = deep;
    }

    @Override
    public List<T> getChildren() {
        return children;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setChildren(List<? extends Tree<K>> children) {
        this.children = (List<T>) children;
    }


}
