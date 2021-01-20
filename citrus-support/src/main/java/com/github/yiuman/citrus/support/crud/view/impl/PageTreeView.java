package com.github.yiuman.citrus.support.crud.view.impl;

import com.github.yiuman.citrus.support.crud.view.EditableView;
import com.github.yiuman.citrus.support.model.Tree;

/**
 * 页面中拥有编辑器的树形视图
 *
 * @author yiuman
 * @date 2021/1/20
 */
public class PageTreeView<T extends Tree<?>> extends SimpleTreeView<T> implements EditableView {

    /**
     * 编辑器，用于实例的编辑（新增、修改）
     */
    private Object editableView;

    public PageTreeView() {
    }


    public PageTreeView(boolean displayRoot) {
        super(displayRoot);
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
