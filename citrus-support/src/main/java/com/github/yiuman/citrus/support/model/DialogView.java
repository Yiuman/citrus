package com.github.yiuman.citrus.support.model;

import com.github.yiuman.citrus.support.widget.Widget;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

/**
 * 对话框
 *
 * @author yiuman
 * @date 2020/5/9
 */
public class DialogView {

    private String title;

    private String color = "primary lighten-3 ";

    /**
     * 宽度
     */
    private Integer width = 800;

    /**
     * 是否全屏
     */
    private boolean fullscreen;

    /**
     * 编辑的字段
     * Key：为字段名，Value为对应的组件
     */
    private List<EditField> editFields = new ArrayList<>();

    public DialogView() {
    }

    public DialogView(String title) {
        this.title = title;
    }

    public DialogView(List<EditField> editFields) {
        this.editFields = editFields;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public List<EditField> getEditFields() {
        return editFields;
    }

    public void setEditFields(List<EditField> editFields) {
        this.editFields = editFields;
    }

    public void addEditField(EditField editField) {
        this.editFields.add(editField);
    }

    public void addEditField(String text, String name) {
        this.editFields.add(new EditField(text, name));
    }

    public void addEditField(String text, String name, Widget<?> widget) {
        this.editFields.add(new EditField(text, name, widget));
    }
}
