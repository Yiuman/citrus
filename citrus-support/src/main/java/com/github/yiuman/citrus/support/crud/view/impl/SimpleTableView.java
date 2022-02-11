package com.github.yiuman.citrus.support.crud.view.impl;

import cn.hutool.core.collection.CollUtil;
import com.github.yiuman.citrus.support.crud.view.CheckboxTableView;
import com.github.yiuman.citrus.support.crud.view.DataView;
import com.github.yiuman.citrus.support.crud.view.RecordExtender;
import com.github.yiuman.citrus.support.model.FieldFunction;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.widget.BaseColumn;
import com.github.yiuman.citrus.support.widget.Column;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 基本的表格
 *
 * @param <T> 视图实体类型
 * @author yiuman
 * @date 2021/1/19
 */
public class SimpleTableView<T> extends BaseActionableView implements CheckboxTableView, RecordExtender<T>, DataView<Page<T>> {

    private boolean checkable = true;

    private Page<T> data;

    /**
     * 表头、列
     */
    private List<Column> columns;

    /**
     * 处理字段的Function集合
     */
    private List<FieldFunction<T, ?>> fieldFunctions;

    public SimpleTableView() {
    }

    public SimpleTableView(boolean checkable) {
        this.checkable = checkable;
    }

    @Override
    public boolean getCheckable() {
        return checkable;
    }

    @Override
    public Page<T> getData() {
        if (Objects.nonNull(data) && CollUtil.isNotEmpty(data.getRecords())) {
            Map<String, Map<String, Object>> extension = new HashMap<>();
            data.getRecords().forEach(record -> {
                Map<String, Object> result = apply(record);
                if (Objects.nonNull(result)) {
                    extension.put(data.key(record), result);
                }
            });
            data.setExtension(extension);
        }

        return data;
    }

    @Override
    public void setData(Page<T> data) {
        this.data = data;
    }

    @Override
    public List<? extends Column> getColumns() {
        return columns;
    }

    public Column addColumn(Column column) {
        this.columns = Optional.ofNullable(this.columns).orElse(new ArrayList<>());
        this.columns.add(column);
        return column;
    }

    public Column addColumn(String text, String field) {
        return addColumn(BaseColumn.builder().text(text).model(field).build());
    }

    public Column addColumn(String text, String field, boolean sortable) {
        return addColumn(BaseColumn.builder().text(text).model(field).sortable(sortable).build());
    }

    public Column addColumn(String text, Function<T, ?> func) {
        String defaultExtendFieldName = getDefaultExtendFieldName();
        addFieldFunctions(defaultExtendFieldName, func);
        return addColumn(text, defaultExtendFieldName);
    }

    public Column addColumn(String text, String field, Function<T, ?> func) {
        addFieldFunctions(field, func);
        return addColumn(text, field);
    }

    public void addFieldFunctions(String name, Function<T, ?> func) {
        this.fieldFunctions = Optional.ofNullable(this.fieldFunctions).orElse(new ArrayList<>());
        this.fieldFunctions.add(new FieldFunction<>(name, func));
    }

    private String getDefaultExtendFieldName() {
        this.fieldFunctions = Optional.ofNullable(this.fieldFunctions).orElse(new ArrayList<>());
        return String.format("ext_field_%s", this.fieldFunctions.size() + 1);
    }

    @Override
    public Map<String, Object> apply(T object) {
        if (CollectionUtils.isEmpty(this.fieldFunctions)) {
            return null;
        }
        Map<String, Object> funcExecutedMap = new ConcurrentHashMap<>(fieldFunctions.size());
        fieldFunctions.parallelStream().forEach(fieldFunc
                -> funcExecutedMap.put(fieldFunc.getFiledName(), fieldFunc.getFunction().apply(object)));
        return funcExecutedMap;
    }
}
