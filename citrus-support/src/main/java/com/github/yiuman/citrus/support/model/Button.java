package com.github.yiuman.citrus.support.model;

/**
 * 按钮
 *
 * @author yiuman
 * @date 2020/5/8
 */
public class Button {

    private String text;

    private String action;

    private String color;

    private String icon;

    public Button() {
    }

    public Button(String text, String action) {
        this.text = text;
        this.action = action;
    }

    public Button(String text, String action, String color) {
        this.text = text;
        this.action = action;
        this.color = color;
    }

    public Button(String text, String action, String color,String icon ) {
        this.text = text;
        this.action = action;
        this.icon = icon;
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
