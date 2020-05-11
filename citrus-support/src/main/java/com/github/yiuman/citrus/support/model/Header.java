package com.github.yiuman.citrus.support.model;

import lombok.Builder;

/**
 * 表头
 *
 * @author yiuman
 * @date 2020/5/7
 */
@Builder
public class Header {

    /**
     * 文本
     */
    private String text;

    /**
     * 字段名
     */
    private String value;

    private Align align = Align.center;

    private Boolean sortable = false;

    private Integer width;

    public Header() {
    }

    public Header(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public Header(String text, String value, Boolean sortable) {
        this.text = text;
        this.value = value;
        this.sortable = sortable;
    }

    public Header(String text, String value, Align align, Boolean sortable, Integer width) {
        this.text = text;
        this.value = value;
        this.align = align;
        this.sortable = sortable;
        this.width = width;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Align getAlign() {
        return align;
    }

    public void setAlign(Align align) {
        this.align = align;
    }

    public Boolean getSortable() {
        return sortable;
    }

    public void setSortable(Boolean sortable) {
        this.sortable = sortable;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    enum Align {

        start,

        center,

        end
    }
}
