package com.github.yiuman.citrus.support.crud.view.impl;

import com.github.yiuman.citrus.support.crud.view.TreeView;
import com.github.yiuman.citrus.support.model.Button;
import com.github.yiuman.citrus.support.model.Tree;
import com.github.yiuman.citrus.support.widget.Inputs;
import com.github.yiuman.citrus.support.widget.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * CRUD操作时显示的页面树形结构以及页面组件
 *
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

    /**
     * 顶部的控件集合
     */
    private List<Object> widgets = new ArrayList<>();

    private T tree;

    /**
     * 顶部按钮
     */
    private List<Button> buttons = new ArrayList<>();

    /**
     * 列的按钮，列的事件，行内操作
     * 此处可用El表达式
     */
    private List<Button> actions = new ArrayList<>();

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
    public List<Object> getWidgets() {
        return widgets;
    }

    @Override
    public void setWidgets(List<Object> widgets) {
        this.widgets = widgets;
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
    public List<Button> getButtons() {
        return buttons;
    }

    @Override
    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

    @Override
    public List<Button> getActions() {
        return actions;
    }

    @Override
    public void setActions(List<Button> actions) {
        this.actions = actions;
    }

    @Override
    public <W extends Widget<?>> void addWidget(W widget) {
        addWidget(widget, false);
    }

    @Override
    public void addWidget(String text, String fieldName) {
        Inputs inputs = new Inputs(text, fieldName);
        addWidget(inputs);
    }

    @Override
    public <W extends Widget<?>> void addWidget(W widget, boolean refresh) {
        if (refresh || !widgets.contains(widget)) {
            int indexOf = widgets.indexOf(widget);
            if (indexOf != -1) {
                widgets.set(indexOf, widget);
            } else {
                widgets.add(widget);
            }
        }
    }

    @Override
    public void addButton(Button button) {
        buttons.add(button);
    }

    public void addButton(List<Button> buttons) {
        buttons.forEach(this::addButton);
    }

    @Override
    public void addAction(Button button) {
        actions.add(button);
    }

    public void addAction(String text, String action) {
        Button button = new Button(text, action);
        button.setScript(true);
        actions.add(button);
    }

}
