package com.github.yiuman.citrus.support.widget;

/**
 * 控件基类
 *
 * @author yiuman
 * @date 2020/5/6
 */
public abstract class BaseWidget<M> implements Widget<M> {

    /**
     * 文本
     */
    private String text;

    /**
     * 键
     */
    private String key;

    /**
     * 值
     */
    private M model;

    public BaseWidget() {
    }

    public BaseWidget(String text, String key, M model) {
        this.text = text;
        this.key = key;
        this.model = model;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public M getModel() {
        return this.model;
    }

    public void setModel(M model) {
        this.model = model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseWidget)) return false;

        BaseWidget<?> that = (BaseWidget<?>) o;

        return key != null ? key.equals(that.key) : that.key == null;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }
}
