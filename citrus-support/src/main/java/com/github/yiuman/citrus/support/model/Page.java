package com.github.yiuman.citrus.support.model;

import com.github.yiuman.citrus.support.crud.view.RecordExtender;
import com.github.yiuman.citrus.support.crud.view.TableView;
import com.github.yiuman.citrus.support.crud.view.impl.SimpleTableView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页页面对象
 *
 * @author yiuman
 * @date 2020/5/7
 */
@Slf4j
public class Page<T> extends com.baomidou.mybatisplus.extension.plugins.pagination.Page<T>
        implements Key<T, String> {

    /**
     * 主键属性名称
     */
    private String itemKey;

    /**
     * 实体的主键属性
     */
    private Field keyFiled;

    /**
     * 记录的扩展属性，key为记录主键值，value则是需要扩展的属性，
     */
    private Map<String, Map<String, Object>> recordExtend;

    /**
     * 一个视图的描述对象，如表格
     *
     * @see SimpleTableView
     * @see TableView
     * @see RecordExtender
     */
    private Object view;

    public Page() {
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }


    public Map<String, Map<String, Object>> getRecordExtend() {
        return recordExtend;
    }

    public void setRecordExtend(Map<String, Map<String, Object>> recordExtend) {
        this.recordExtend = recordExtend;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> getRecords() {
        List<T> records = super.getRecords();
        if (this.recordExtend == null && !StringUtils.isEmpty(itemKey) && view instanceof RecordExtender) {
            this.recordExtend = new HashMap<>(records.size());
            RecordExtender<T> recordExtender = (RecordExtender<T>) view;
            records.forEach(record -> recordExtend.put(key(record), recordExtender.apply(record)));
        }
        return records;
    }

    public Object getView() {
        return view;
    }

    public void setView(Object view) {
        this.view = view;
    }

    @Override
    public String key(T entity) {
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
