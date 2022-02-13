package com.github.yiuman.citrus.support.crud.view.impl;

import com.github.yiuman.citrus.support.crud.view.TreeView;
import com.github.yiuman.citrus.support.model.Tree;
import com.github.yiuman.citrus.support.widget.Button;
import com.github.yiuman.citrus.support.widget.Inputs;
import com.github.yiuman.citrus.support.widget.Widget;

import java.util.List;

/**
 * CRUD操作时显示的页面树形结构以及页面组件
 *
 * @param <T> 树形视图的树形实体类型
 * @author yiuman
 * @date 2020/5/13
 */
public class SimpleTreeView<T extends Tree<?>> extends BaseActionableView implements TreeView<T> {

    private boolean displayRoot = true;

    private boolean lazy;

    /**
     * 实体的主键
     */
    private String itemKey = "id";

    /**
     * 显示的名称
     */
    private String itemText = "name";


    private T tree;

    /**
     * 列的按钮，列的事件，行内操作
     * 此处可用El表达式
     */
    public SimpleTreeView() {
    }

    public SimpleTreeView(boolean displayRoot) {
        this.displayRoot = displayRoot;
    }

    @Override
    public boolean isDisplayRoot() {
        return displayRoot;
    }

    public void setDisplayRoot(boolean displayRoot) {
        this.displayRoot = displayRoot;
    }

    @Override
    public boolean isLazy() {
        return lazy;
    }

    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }

    @Override
    public String getItemKey() {
        return itemKey;
    }

    @Override
    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    @Override
    public String getItemText() {
        return itemText;
    }

    @Override
    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    @Override
    public T getTree() {
        return tree;
    }

    @Override
    public void setTree(T tree) {
        this.tree = tree;
    }

    @Override
    public <W extends Widget<W, ?>> void addWidget(W widget) {
        addWidget(widget, false);
    }

    @Override
    public void addWidget(String text, String fieldName) {
        Inputs inputs = new Inputs(text, fieldName);
        addWidget(inputs);
    }

    public void addButton(List<Button> buttons) {
        buttons.forEach(this::addButton);
    }

}
