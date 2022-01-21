package com.github.yiuman.citrus.support.widget;

/**
 * 输入控件
 *
 * @author yiuman
 * @date 2020/5/6
 */
public class Inputs extends BaseWidget<Inputs, String> {

    private boolean clearable = true;

    /**
     * 类型，如number等
     */
    private String type;

    /**
     *
     */
    private Integer counter;

    /**
     * '<input>'的placeholder
     */
    private String placeholder;

    public Inputs() {
    }

    public Inputs(String text, String key) {
        super(text, key, null);
    }

    public boolean isClearable() {
        return clearable;
    }

    public void setClearable(boolean clearable) {
        this.clearable = clearable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Inputs type(String type) {
        setType(type);
        return this;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public Inputs counter(Integer counter) {
        setCounter(counter);
        return this;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public Inputs placeholder(String placeholder) {
        setPlaceholder(placeholder);
        return this;
    }

    @Override
    public String getWidgetName() {
        return "v-text-field";
    }
}
