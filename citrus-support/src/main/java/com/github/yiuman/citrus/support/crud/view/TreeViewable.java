package com.github.yiuman.citrus.support.crud.view;

import com.github.yiuman.citrus.support.model.Tree;

/**
 * 可树形视化
 *
 * @param <T> 树形数据
 * @author yiuman
 * @date 2022/1/23
 */
public interface TreeViewable<T extends Tree<?>> {
    /**
     * 创建树形视图
     *
     * @param data 数据
     * @return 视图对象
     */
    <VIEW extends TreeView<T>> VIEW showTreeView(T data);
}