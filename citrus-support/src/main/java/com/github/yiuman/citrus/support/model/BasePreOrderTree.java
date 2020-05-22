package com.github.yiuman.citrus.support.model;


/**
 * 预排序树形结构，左右值算法
 *
 * @author yiuman
 * @date 2020/3/30
 */
public abstract class BasePreOrderTree<T extends BasePreOrderTree<T, K>, K> extends BaseTree<T, K> implements PreOrderTree<K> {

    private Integer leftValue;

    private Integer rightValue;

    private Integer deep;

    public BasePreOrderTree() {
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


}
