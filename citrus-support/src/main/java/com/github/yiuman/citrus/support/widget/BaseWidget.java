package com.github.yiuman.citrus.support.widget;

import java.util.Objects;

/**
 * 控件基类
 *
 * @param <M> 小部件值模型类型
 * @author yiuman
 * @date 2020/5/6
 */
public abstract class BaseWidget<M> implements Widget<M> {

    /**
     * 文本
     */
    private String text;

    /**
     * 键，用于控件值对应的KEY
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
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaseWidget)) {
            return false;
        }

        BaseWidget<?> that = (BaseWidget<?>) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }
}
