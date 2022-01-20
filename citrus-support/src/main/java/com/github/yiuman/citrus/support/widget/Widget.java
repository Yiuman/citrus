package com.github.yiuman.citrus.support.widget;

/**
 * @param <W> 小部件
 * @param <M> 模型
 * @author yiuman
 * @date 2022/1/20
 */
public interface Widget<W extends Propertied<W>, M> extends WidgetModel<M>, Propertied<W> {
}