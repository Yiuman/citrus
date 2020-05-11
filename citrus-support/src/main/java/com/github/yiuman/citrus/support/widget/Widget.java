package com.github.yiuman.citrus.support.widget;

/**
 * 控件/小部件
 *
 * @author yiuman
 * @date 2020/5/6
 */
public interface Widget<M> {

    String getKey();

    String getText();

    M getModel();

    String getWidgetName();
}