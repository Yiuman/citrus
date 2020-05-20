package com.github.yiuman.citrus.support.model;

import com.github.yiuman.citrus.support.widget.Inputs;
import com.github.yiuman.citrus.support.widget.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CRUD操作时显示的页面树形结构以及页面组件
 *
 * @author yiuman
 * @date 2020/5/13
 */
public class TreeDisplay<T> {

    private boolean lazy;

    /**
     * 实体的主键
     */
    private String itemKey = "id";

    /**
     * 显示的名称
     */
    private String itemText = "name";

    /**
     * 顶部的控件集合
     */
    private List<Object> widgets = new ArrayList<>();

    private T tree;


    /**
     * 顶部按钮
     */
    private List<Button> buttons = new ArrayList<>();

    /**
     * 记录的扩展属性，key为记录主键值，value则是需要扩展的属性，
     */
    private Map<String, Map<String, Object>> recordExtend = new HashMap<>();

    /**
     * 记录执行器，每次获取记录前，则会执行此执行器进行处理
     */
    private List<FieldFunction<T>> recordFunctions = new ArrayList<>();

    /**
     * 对话框（新增、编辑页面的定义）
     */
    private DialogView dialogView;

    public TreeDisplay() {
    }

    public boolean isLazy() {
        return lazy;
    }

    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public List<Object> getWidgets() {
        return widgets;
    }

    public void setWidgets(List<Object> widgets) {
        this.widgets = widgets;
    }

    public T getTree() {
        return tree;
    }

    public void setTree(T tree) {
        this.tree = tree;
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

    public Map<String, Map<String, Object>> getRecordExtend() {
        return recordExtend;
    }

    public void setRecordExtend(Map<String, Map<String, Object>> recordExtend) {
        this.recordExtend = recordExtend;
    }

    public List<FieldFunction<T>> getRecordFunctions() {
        return recordFunctions;
    }

    public void setRecordFunctions(List<FieldFunction<T>> recordFunctions) {
        this.recordFunctions = recordFunctions;
    }

    public DialogView getDialogView() {
        return dialogView;
    }

    public void setDialogView(DialogView dialogView) {
        this.dialogView = dialogView;
    }

    public <W extends Widget<?>> void addWidget(W widget) {
        addWidget(widget, false);
    }

    public void addWidget(String text, String fieldName) {
        Inputs inputs = new Inputs(text, fieldName);
        addWidget(inputs);
    }

    public <W extends Widget<?>> void addWidget(W widget, boolean refresh) {
        if (refresh || !widgets.contains(widget)) {
            int indexOf = widgets.indexOf(widget);
            if (indexOf != -1) {
                widgets.set(indexOf, widget);
            } else {
                widgets.add(widget);
            }
        }
    }

    public void addButton(Button button) {
        buttons.add(button);
    }

    public void addButton(List<Button> buttons) {
        buttons.forEach(this::addButton);
    }
}
