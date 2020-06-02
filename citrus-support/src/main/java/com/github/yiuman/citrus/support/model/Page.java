package com.github.yiuman.citrus.support.model;

import com.github.yiuman.citrus.support.widget.Inputs;
import com.github.yiuman.citrus.support.widget.Widget;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

/**
 * @author yiuman
 * @date 2020/5/7
 */
public class Page<T> extends com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> {

    /**
     * 主键属性名称
     */
    private String itemKey;

    /**
     * 实体的主键属性
     */
    private Field keyFiled;

    /**
     * 顶部的控件集合
     */
    private List<Object> widgets = new ArrayList<>();

    /**
     * 列头，表头
     */
    private List<Header> headers = new ArrayList<>();

    /**
     * 顶部按钮
     */
    private List<Button> buttons = new ArrayList<>();

    /**
     * 列的按钮，列的事件
     */
    private List<Button> actions = new ArrayList<>();

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

    public Page() {
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public List<Object> getWidgets() {
        return widgets;
    }

    public void setWidgets(List<Object> widgets) {
        this.widgets = widgets;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

    public List<Button> getActions() {
        return actions;
    }

    public void setActions(List<Button> actions) {
        this.actions = actions;
    }

    public DialogView getDialogView() {
        return dialogView;
    }

    public void setDialogView(DialogView dialogView) {
        this.dialogView = dialogView;
    }

    public Map<String, Map<String, Object>> getRecordExtend() {

        return recordExtend;
    }

    public void setRecordExtend(Map<String, Map<String, Object>> recordExtend) {
        this.recordExtend = recordExtend;
    }

    @Override
    public List<T> getRecords() {
        final List<T> records = super.getRecords();
        this.recordFunctions.forEach(func -> records.forEach(record -> {
            Map<String, Object> objectObjectHashMap = Optional.ofNullable(this.recordExtend.get(getKey(record))).orElse(new HashMap<>(1));
            objectObjectHashMap.put(func.getFiledName(), func.getFunction().apply(record));
            this.recordExtend.put(getKey(record), objectObjectHashMap);
        }));
        return records;
    }

    public void addHeader(String text, String field) {
        headers.add(new Header(text, field));
    }

    public void addHeader(String text, String field, boolean sortable) {
        headers.add(new Header(text, field, sortable));
    }

    public void addHeader(String text, String field, Function<T, Object> func) {
        headers.add(new Header(text, field));
        recordFunctions.add(new FieldFunction<>(field, func));
    }

    public void addHeader(Header header) {
        headers.add(header);
    }

    public <W extends Widget<?>> void addWidget(W widget) {
        addWidget(widget, false);
    }

    public <W extends Widget<?>> void addWidget(String text,String fieldName){
        Inputs inputs = new Inputs(text, fieldName);
        addWidget(inputs, false);
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

    public void addAction(Button button) {
        actions.add(button);
    }

    public void addActions(List<Button> actions) {
        actions.forEach(this::addAction);
    }

    private String getKey(T entity) {
        try {
            if (keyFiled == null) {
                keyFiled = ReflectionUtils.findField(entity.getClass(), itemKey);
                keyFiled.setAccessible(true);
            }
            return keyFiled.get(entity).toString();
        } catch (Exception ex) {
            return entity.toString();
        }

    }
}
