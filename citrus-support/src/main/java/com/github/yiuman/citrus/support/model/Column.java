package com.github.yiuman.citrus.support.model;

import com.github.yiuman.citrus.support.widget.WidgetModel;
import lombok.Builder;
import lombok.Data;

/**
 * 表头
 *
 * @author yiuman
 * @date 2020/5/7
 */
@Builder(toBuilder = true)
@Data
public class Column {

    /**
     * 文本
     */
    private String text;

    /**
     * 字段名
     */
    private String value;

    @Builder.Default
    private Align align = Align.start;

    @Builder.Default
    private Boolean sortable = false;

    private Integer width;

    private boolean hidden;

    /**
     * 小部件
     */
    private WidgetModel<?> widgetModel;

    /**
     * 排列格式
     */
    public enum Align {

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
