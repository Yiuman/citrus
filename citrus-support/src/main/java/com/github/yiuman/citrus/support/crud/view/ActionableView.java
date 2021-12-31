package com.github.yiuman.citrus.support.crud.view;


import com.github.yiuman.citrus.support.widget.Button;

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
    List<Object> getWidgets();

    /**
     * 设置小部件集合
     *
     * @param widgets 小部件集合
     */
    void setWidgets(List<Object> widgets);

    /**
     * 获取页面的按钮集合
     *
     * @return 按钮集合
     */
    List<Button> getButtons();

    /**
     * 设置按钮集合
     *
     * @param buttons 按钮集合
     */
    void setButtons(List<Button> buttons);

    /**
     * 页面的行内操作集合
     *
     * @return 行内操作
     */
    List<Button> getActions();

    /**
     * 设置动作集合
     *
     * @param actions 动作集合
     */
    void setActions(List<Button> actions);

}