package com.github.yiuman.citrus.support.widget;

/**
 * 日期选择
 *
 * @author yiuman
 * @date 2020/10/21
 */
public class DatePicker extends Inputs {

    public DatePicker() {
    }

    public DatePicker(String text, String key) {
        super(text, key);
    }

    @Override
    public String getWidgetName() {
        return "date-picker";
    }
}
