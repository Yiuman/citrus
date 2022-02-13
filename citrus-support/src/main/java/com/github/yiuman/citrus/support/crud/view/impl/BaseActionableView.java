package com.github.yiuman.citrus.support.crud.view.impl;

import com.github.yiuman.citrus.support.crud.view.ActionableView;
import com.github.yiuman.citrus.support.widget.Inputs;
import com.github.yiuman.citrus.support.widget.Widget;
import com.github.yiuman.citrus.support.widget.WidgetModel;

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
     * @see WidgetModel
     */
    private List<Widget<?, ?>> widgets = new ArrayList<>();

    /**
     * 页面的按钮
     */
    private List<Widget<?, ?>> buttons = new ArrayList<>();

    public BaseActionableView() {
    }

    @Override
    public List<Widget<?, ?>> getWidgets() {
        return widgets;
    }

    @Override
    public void setWidgets(List<Widget<?, ?>> widgets) {
        this.widgets = widgets;
    }

    public <W extends Widget<W, ?>> void addWidget(W widget) {
        addWidget(widget, false);
    }

    public void addWidget(String text, String fieldName) {
        Inputs inputs = new Inputs(text, fieldName);
        addWidget(inputs, false);
    }

    public <W extends Widget<W, ?>> void addWidget(W widget, boolean refresh) {
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
    public List<Widget<?, ?>> getButtons() {
        return buttons;
    }

    @Override
    public void setButtons(List<Widget<?, ?>> buttons) {
        this.buttons = buttons;
    }

    public void addButton(Widget<?, ?>... button) {
        Arrays.stream(button).forEach(this::addButton);
    }

    public void addButton(Collection<Widget<?, ?>> buttons) {
        buttons.forEach(this::addButton);
    }

    public void addButton(Widget<?, ?> button) {
        this.buttons = Optional.ofNullable(this.buttons).orElse(new ArrayList<>());
        this.buttons.add(button);
    }

}
