package com.github.yiuman.citrus.support.widget;

/**
 * 输入控件
 *
 * @author yiuman
 * @date 2020/5/6
 */
public class Inputs extends BaseWidget<String> {

    private boolean clearable = true;

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

    @Override
    public String getWidgetName() {
        return "v-text-field";
    }
}
