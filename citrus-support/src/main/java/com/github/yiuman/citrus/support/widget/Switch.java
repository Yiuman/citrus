/**
 *
 */
package com.github.yiuman.citrus.support.widget;

/**
 * @author xmen
 * 2021年7月29日
 */
public class Switch extends BaseWidgetModel<Switch, Boolean> {

    @Override
    public String getWidgetName() {
        return "switch";
    }

    /**
     * @param text
     * @param key
     * @param model
     */
    public Switch(String text, String key) {
        super(text, key, null);
    }


}
