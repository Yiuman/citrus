package com.github.yiuman.citrus.support.model;

import lombok.Builder;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 按钮
 *
 * @author yiuman
 * @date 2020/5/8
 */
public class Button {

    /**
     * 按钮文本
     */
    private String text;

    /**
     * 执行的时间名或者脚本
     */
    private String action;

    /**
     * 按钮颜色
     */
    private String color;

    /**
     * 按钮图标
     */
    private String icon;


    /**
     * 按钮集合，如不为空，则为按钮组
     */
    private List<Button> actions;

    /**
     * 是否脚本，若为脚本，action将动态构建执行
     */
    private boolean script = false;

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

    public Button(String text, String action, String color, String icon) {
        this.text = text;
        this.action = action;
        this.icon = icon;
        this.color = color;
    }

    public Button(String text, String color, String icon, Button... actions) {
        this.text = text;
        this.icon = icon;
        this.color = color;
        this.actions = Arrays.asList(actions);
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

    public boolean isGroup() {
        return !CollectionUtils.isEmpty(actions);
    }

    public List<Button> getActions() {
        return actions;
    }

    public void setActions(List<Button> actions) {
        this.actions = actions;
    }

    public boolean isScript() {
        return script;
    }

    public void setScript(boolean script) {
        this.script = script;
    }
}
