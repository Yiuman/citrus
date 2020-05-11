package com.github.yiuman.citrus.support.widget;

/**
 * 输入控件
 *
 * @author yiuman
 * @date 2020/5/6
 */
public class Inputs extends BaseWidget<String> {

    public Inputs() {
    }

    public Inputs(String text, String key) {
        super(text, key, null);
    }


    @Override
    public String getWidgetName() {
        return "v-text-field";
    }
}
