package com.github.yiuman.citrus.support.crud.view.impl;

import com.github.yiuman.citrus.support.crud.view.ActionableView;
import com.github.yiuman.citrus.support.model.Button;
import com.github.yiuman.citrus.support.widget.Inputs;
import com.github.yiuman.citrus.support.widget.Widget;

import java.util.*;

/**
 * 可执行操作的视图基类
 *
 * @author yiuman
 * @date 2021/1/20
 */
public class BaseActionableView implements ActionableView {

    /**
     * 控件,如搜索框、下拉选择等，用于页面的搜索等操作
     *
     * @see com.github.yiuman.citrus.support.widget.Widget
     */
    private List<Object> widgets;

    /**
     * 页面的按钮
     */
    private List<Button> buttons;

    /**
     * 列的按钮，列的事件，行内操作
     */
    private List<Button> actions;

    public BaseActionableView() {
    }


    @Override
    public List<Object> getWidgets() {
        return widgets;
    }

    @Override
    public void setWidgets(List<Object> widgets) {
        this.widgets = widgets;
    }

    public <W extends Widget<?>> void addWidget(W widget) {
        addWidget(widget, false);
    }

    public void addWidget(String text, String fieldName) {
        Inputs inputs = new Inputs(text, fieldName);
        addWidget(inputs, false);
    }

    public <W extends Widget<?>> void addWidget(W widget, boolean refresh) {
        this.widgets = Optional.ofNullable(this.widgets).orElse(new ArrayList<>());
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
    public List<Button> getButtons() {
        return buttons;
    }

    @Override
    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

    public void addButton(Button... button) {
        Arrays.stream(button).forEach(this::addButton);
    }

    public void addButton(Collection<Button> buttons) {
        buttons.forEach(this::addButton);
    }

    public void addButton(Button button) {
        this.buttons = Optional.ofNullable(this.buttons).orElse(new ArrayList<>());
        this.buttons.add(button);
    }

    @Override
    public List<Button> getActions() {
        return actions;
    }

    @Override
    public void setActions(List<Button> actions) {
        this.actions = actions;
    }

    public void addAction(Button... action) {
        Arrays.stream(action).forEach(this::addAction);
    }

    public void addAction(Collection<Button> actions) {
        actions.forEach(this::addAction);
    }

    public void addAction(Button action) {
        this.actions = Optional.ofNullable(this.actions).orElse(new ArrayList<>());
        this.actions.add(action);
    }


}
