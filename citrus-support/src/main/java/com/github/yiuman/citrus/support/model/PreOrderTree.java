package com.github.yiuman.citrus.support.model;

/**
 * 预遍历排序树，左右值算法
 *
 * @param <K> 树主键类型
 * @author yiuman
 * @date 2020/5/22
 */
public interface PreOrderTree<K> extends Tree<K> {

    /**
     * 获取当前节点左值
     *
     * @return 左值
     */
    Integer getLeftValue();

    /**
     * 设置左值
     *
     * @param leftValue 左值
     */
    void setLeftValue(Integer leftValue);

    /**
     * 获取当前节点右值
     *
     * @return 右值
     */
    Integer getRightValue();

    /**
     * 设置右值
     *
     * @param rightValue 右值
     */
    void setRightValue(Integer rightValue);

    /**
     * 获取当前深度
     *
     * @return 深度
     */
    Integer getDeep();

    /**
     * 设置当前深度
     *
     * @param deep 深度
     */
    void setDeep(Integer deep);
}