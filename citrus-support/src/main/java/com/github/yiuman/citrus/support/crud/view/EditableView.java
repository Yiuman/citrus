package com.github.yiuman.citrus.support.crud.view;

/**
 * 可编辑的页面
 *
 * @author yiuman
 * @date 2021/1/20
 */
public interface EditableView {

    /**
     * 获取可编辑视图对象
     *
     * @return 可编辑视图
     */
    Object getEditableView();

    /**
     * 设置可编辑视图
     *
     * @param object 可编辑视图对象
     */
    void setEditableView(Object object);
}