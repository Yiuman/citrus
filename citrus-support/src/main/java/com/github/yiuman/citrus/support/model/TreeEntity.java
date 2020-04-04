package com.github.yiuman.citrus.support.model;

/**
 * 树形结构，左右值算法
 *
 * @author yiuman
 * @date 2020/3/30
 */
public class TreeEntity {

    private Integer leftValue;

    private Integer rightValue;

    public TreeEntity() {
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
}
