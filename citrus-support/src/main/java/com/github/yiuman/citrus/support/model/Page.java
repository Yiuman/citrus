package com.github.yiuman.citrus.support.model;

import com.github.yiuman.citrus.support.widget.Inputs;
import com.github.yiuman.citrus.support.widget.Widget;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页页面对象
 *
 * @author yiuman
 * @date 2020/5/7
 */
@Slf4j
public class Page<T> extends com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> {

    /**
     * 是否可选
     */
    private Boolean hasSelect = true;

    /**
     * 主键属性名称
     */
    private String itemKey;

    /**
     * 实体的主键属性
     */
    private Field keyFiled;

    /**
     * 控件集合
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
     * 列的按钮，列的事件，行内操作
     */
    private List<Button> actions = new ArrayList<>();

    /**
     * 记录的扩展属性，key为记录主键值，value则是需要扩展的属性，
     */
    private Map<String, Map<String, Object>> recordExtend = new HashMap<>();

    /**
     * 记录执行器，每次获取记录前，则会执行此执行器进行处理
     */
    private final List<FieldFunction<T, ?>> recordFunctions = new ArrayList<>();

    /**
     * 事件执行处理器
     * 某些事件可能与记录相关
     */
    private final List<Function<T, Button>> actionFunctions = new ArrayList<>();

    /**
     * 对话框（新增、编辑页面的定义）
     */
    private DialogView dialogView;

    public Page() {
    }

    public Boolean getHasSelect() {
        return hasSelect;
    }

    public void setHasSelect(Boolean hasSelect) {
        this.hasSelect = hasSelect;
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

    public Header addHeader(String text, String field) {
        Header header = new Header(text, field);
        headers.add(header);
        return header;
    }

    public Header addHeader(String text, String field, boolean sortable) {
        Header header = new Header(text, field, sortable);
        headers.add(header);
        return header;
    }

    public Header addHeader(String text, String field, Function<T, Object> func) {
        Header header = new Header(text, field);
        headers.add(header);
        recordFunctions.add(new FieldFunction<>(field, func));
        return header;
    }

    public void addCompositionHeader(List<Header> headerGroup, Function<T, Map<String, Object>> mapFunc) {
        headers.addAll(headerGroup);
        String fieldFunctionKey = headerGroup.stream().map(Header::getValue).collect(Collectors.joining("|"));
        recordFunctions.add(new FieldFunction<>(fieldFunctionKey, mapFunc));
    }

    public void addHeader(Header header) {
        headers.add(header);
    }

    public <W extends Widget<?>> void addWidget(W widget) {
        addWidget(widget, false);
    }

    public void addWidget(String text, String fieldName) {
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

    public void addAction(Function<T, Button> actionFunc) {
        actionFunctions.add(actionFunc);
    }

    public void addActions(List<Button> actions) {
        actions.forEach(this::addAction);
    }

    public void beforeShow() {
        initFunctionalRecords();
        initFunctionalActions();
    }

    /**
     * 初始化记录的扩展字段
     * 如:records:[{key:'1',aField:xxx,bField:xxx}]
     * 若需要扩展上面的记录字段，如想加一个cField;
     * 则扩展后为
     * {
     *     records:[{key:'1',aField:xxx,bField:xxx}],
     *     extends:{
     *         '1':{
     *             cField:xxxx
     *         }
     *     }
     * }
     */
    public void initFunctionalRecords() {
        if (!StringUtils.isEmpty(itemKey)) {
            getRecords().forEach(this::initSingleFunctionalRecord);
        }
    }

    /**
     * 函数初始化操作指令
     */
    public void initFunctionalActions() {
        actionFunctions.forEach(func -> getRecords().forEach(record -> actions.add(func.apply(record))));
    }

    /**
     * 初始化单记录的扩展数据
     *
     * @param record 当前需扩展的记录
     */
    @SuppressWarnings("unchecked")
    public void initSingleFunctionalRecord(T record) {
        String vertical = "|";
        this.recordFunctions.forEach(func -> {
            Map<String, Object> objectObjectHashMap = Optional.ofNullable(this.recordExtend.get(getKey(record))).orElse(new HashMap<>(1));
            String filedName = func.getFiledName();
            Object value = func.getFunction().apply(record);
            if (filedName.contains(vertical)) {
                Map<String, Object> valueMap = (Map<String, Object>) value;
                valueMap.forEach(objectObjectHashMap::put);
            } else {
                objectObjectHashMap.put(func.getFiledName(), value);
            }

            this.recordExtend.put(getKey(record), objectObjectHashMap);
        });
    }

    private String getKey(T entity) {
        try {
            if (keyFiled == null) {
                keyFiled = ReflectionUtils.findField(entity.getClass(), itemKey);
                keyFiled.setAccessible(true);
            }
            return keyFiled.get(entity).toString();
        } catch (Exception ex) {
            log.info("获取主键异常", ex);
            return entity.toString();
        }

    }

}
