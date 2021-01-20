package com.github.yiuman.citrus.support.crud.view;

import com.github.yiuman.citrus.support.model.Tree;

/**
 * 树形视图
 *
 * @author yiuman
 * @date 2021/1/20
 */
public interface TreeView<T extends Tree<?>> extends View {

    /**
     * 是否展示根节点
     *
     * @return true/false
     */
    boolean isDisplayRoot();

    /**
     * 是否懒加载
     *
     * @return true/false
     */
    boolean isLazy();

    /**
     * 获取树
     *
     * @return 树形结构实体
     * @see Tree
     */
    T getTree();

    void setTree(T tree);

    /**
     * 获取实体项的键
     *
     * @return 键的字段名
     */
    String getItemKey();

    void setItemKey(String itemKey);

    /**
     * 获取实体项的展示字段
     *
     * @return 展示的字段名
     */
    String getItemText();

    void setItemText(String itemText);

}