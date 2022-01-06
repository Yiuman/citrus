package com.github.yiuman.citrus.support.widget;

import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 控件基类
 *
 * @param <W> 控件本身
 * @param <M> 小部件值模型类型
 * @author yiuman
 * @date 2020/5/6
 */
@SuperBuilder
@SuppressWarnings("unchecked")
public abstract class BaseWidget<W extends Propertied<W>, M> implements Widget<M>, Propertied<W> {

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

    private final Map<String, Object> properties = new HashMap<>();

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
    public W setProperty(String name, Object value) {
        properties.put(name, value);
        return (W) this;
    }

    @Override
    public Object getProperty(String name) {
        return properties.get(name);
    }

    @Override
    public boolean containsProperty(String name) {
        return properties.containsKey(name);
    }

    @Override
    public Set<String> getProperties() {
        return properties.keySet();
    }

}
