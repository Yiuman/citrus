package com.github.yiuman.citrus.support.crud.view;


import com.github.yiuman.citrus.support.widget.Widget;

import java.util.List;

/**
 * 可执行的视图
 *
 * @author yiuman
 * @date 2021/1/20
 */
public interface ActionableView {

    /**
     * 获取视图的控件集合
     *
     * @return 组件
     */
    List<Widget<?, ?>> getWidgets();

    /**
     * 设置小部件集合
     *
     * @param widgets 小部件集合
     */
    void setWidgets(List<Widget<?, ?>> widgets);

    /**
     * 获取页面的按钮集合
     *
     * @return 按钮集合
     */
    List<Widget<?, ?>> getButtons();

    /**
     * 设置按钮集合
     *
     * @param buttons 按钮集合
     */
    void setButtons(List<Widget<?, ?>> buttons);

}