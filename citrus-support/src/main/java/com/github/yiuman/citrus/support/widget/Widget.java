package com.github.yiuman.citrus.support.widget;

/**
 * 控件/小部件
 *
 * @param <M> 小部件模型类型
 * @author yiuman
 * @date 2020/5/6
 */
public interface Widget<M> {

    /**
     * 控件的主键
     *
     * @return 主键字符串
     */
    String getKey();

    /**
     * 控件暂时的名称
     *
     * @return 控件展示名
     */
    String getText();

    /**
     * 控件的模型，如树，列表等
     *
     * @return 控件所关联的模型实例
     */
    M getModel();

    /**
     * 控件的定义的名称
     *
     * @return 控件名 如：v-selector
     */
    String getWidgetName();
}