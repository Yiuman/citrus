package com.github.yiuman.citrus.support.crud.view.impl;

import com.github.yiuman.citrus.support.crud.view.CheckboxTableView;
import com.github.yiuman.citrus.support.crud.view.RecordExtender;
import com.github.yiuman.citrus.support.model.FieldFunction;
import com.github.yiuman.citrus.support.model.Header;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 基本的表格
 *
 * @param <T> 视图实体类型
 * @author yiuman
 * @date 2021/1/19
 */
public class SimpleTableView<T> extends BaseActionableView implements CheckboxTableView, RecordExtender<T> {

    private boolean checkable = true;

    /**
     * 表头、列
     */
    private List<Header> headers;

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
    public List<Header> getHeaders() {
        return headers;
    }

    public Header addHeader(Header header) {
        this.headers = Optional.ofNullable(this.headers).orElse(new ArrayList<>());
        this.headers.add(header);
        return header;
    }

    public Header addHeader(String text, String field) {
        return addHeader(Header.builder().text(text).value(field).build());
    }

    public Header addHeader(String text, String field, boolean sortable) {
        return addHeader(Header.builder().text(text).value(field).sortable(sortable).build());
    }

    public Header addHeader(String text, Function<T, ?> func) {
        String defaultExtendFieldName = getDefaultExtendFieldName();
        addFieldFunctions(defaultExtendFieldName, func);
        return addHeader(text, defaultExtendFieldName);
    }

    public Header addHeader(String text, String field, Function<T, ?> func) {
        addFieldFunctions(field, func);
        return addHeader(text, field);
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
