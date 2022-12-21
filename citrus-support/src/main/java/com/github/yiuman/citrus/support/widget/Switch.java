package com.github.yiuman.citrus.support.widget;

/**
 * @author xmen
 * 2021年7月29日
 */
public class Switch extends BaseWidget<Switch, Boolean> {

    /**
     * @param text
     * @param key
     * @param model
     */
    public Switch(String text, String key) {
        super(text, key, null);
    }

    @Override
    public String getWidgetName() {
        return "switch";
    }


}
