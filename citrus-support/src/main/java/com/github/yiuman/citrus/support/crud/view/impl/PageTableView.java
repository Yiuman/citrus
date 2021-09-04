package com.github.yiuman.citrus.support.crud.view.impl;

import com.github.yiuman.citrus.support.crud.view.EditableView;

/**
 * 页面中有编辑器的表格视图
 *
 * @param <T> 实体类型
 * @author yiuman
 * @date 2021/1/20
 */
public class PageTableView<T> extends SimpleTableView<T> implements EditableView {

    /**
     * 编辑器，用于实例的编辑（新增、修改）
     */
    private Object editableView;

    public PageTableView() {
    }

    public PageTableView(boolean checkable) {
        super(checkable);
    }

    @Override
    public Object getEditableView() {
        return editableView;
    }

    @Override
    public void setEditableView(Object editableView) {
        this.editableView = editableView;
    }
}
