package com.github.yiuman.citrus.support.widget;

/**
 * 文本
 *
 * @author yiuman
 * @date 2020/9/30
 */
public class Textarea extends Inputs {

    public Textarea() {
    }

    public Textarea(String text, String key) {
        super(text, key);
    }

    @Override
    public String getWidgetName() {
        return "textarea";
    }
}
