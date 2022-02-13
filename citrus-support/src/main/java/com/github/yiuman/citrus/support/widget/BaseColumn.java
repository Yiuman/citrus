package com.github.yiuman.citrus.support.widget;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * @author yiuman
 * @date 2022/2/11
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
public class BaseColumn extends BaseWidget<Column, String> implements Column {

    @Builder.Default
    private Align align = Align.center;

    @Builder.Default
    private Boolean sortable = Boolean.FALSE;

    private Widget<?, ?> widget;

    public BaseColumn() {
    }

    @Override
    public Align getAlign() {
        return align;
    }

    @Override
    public Column align(Align align) {
        this.align = align;
        return this;
    }

    @Override
    public Boolean getSortable() {
        return sortable;
    }

    @Override
    public Column sortable(Boolean sortable) {
        this.sortable = sortable;
        return this;
    }

    @Override
    public Widget<?, ?> getWidget() {
        return widget;
    }

    @Override
    public Column widget(Widget<?, ?> widget) {
        this.widget = widget;
        return this;
    }

}
