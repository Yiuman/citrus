package com.github.yiuman.citrus.support.widget;

/**
 * 列组件
 *
 * @author yiuman
 * @date 2022/2/11
 */
public interface Column extends Widget<Column, String> {

    /**
     * 获取排列格式
     *
     * @return start/center/end
     */
    Align getAlign();

    /**
     * 赋值排列
     *
     * @param align 排列方式
     * @return 列实体
     */
    Column align(Align align);

    /**
     * 是否可排序
     *
     * @return true/false
     */
    Boolean getSortable();

    /**
     * 赋值可排序
     *
     * @param sortable 是否可排序项
     * @return 列实体
     */
    Column sortable(Boolean sortable);

    /**
     * 获取列头对应的小部件
     *
     * @return 小部件实现
     */
    Widget<?, ?> getWidget();

    /**
     * 赋值小部件
     *
     * @param widget 小部件实例
     * @return 列实体
     */
    Column widget(Widget<?, ?> widget);

    /**
     * 获取组件名
     *
     * @return 组件名
     */
    @Override
    default String getWidgetName() {
        return "column";
    }

    enum Align {

        /**
         * 排头
         */
        start,

        /**
         * 居中
         */
        center,

        /**
         * 排尾
         */
        end
    }
}