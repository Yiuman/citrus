package com.github.yiuman.citrus.support.model;

import com.github.yiuman.citrus.support.crud.view.RecordExtender;
import com.github.yiuman.citrus.support.crud.view.TableView;
import com.github.yiuman.citrus.support.crud.view.impl.SimpleTableView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 分页页面对象
 *
 * @param <T> 实体类型
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
    private Map<String, Map<String, Object>> extension;

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

    public Map<String, Map<String, Object>> getExtension() {
        return extension;
    }

    public void setExtension(Map<String, Map<String, Object>> extension) {
        this.extension = extension;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> getRecords() {
        List<T> records = super.getRecords();
        if (Objects.isNull(extension)
                && !ObjectUtils.isEmpty(itemKey)
                && Objects.nonNull(view)
                && view instanceof RecordExtender) {
            this.extension = new HashMap<>(records.size());
            RecordExtender<T> recordExtender = (RecordExtender<T>) view;
            records.forEach(record -> {
                Map<String, Object> result = recordExtender.apply(record);
                if (Objects.nonNull(result)) {
                    extension.put(key(record), result);
                }

            });
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
            if (entity instanceof Map) {
                return ((Map<?, ?>) entity).get(itemKey).toString();
            }

            keyFiled = Optional.ofNullable(keyFiled)
                    .orElse(ReflectionUtils.findField(entity.getClass(), itemKey));
            if (Objects.isNull(keyFiled)) {
                return entity.toString();
            }
            keyFiled.setAccessible(true);
            return keyFiled.get(entity).toString();
        } catch (Exception ex) {
            log.warn("获取主键异常", ex);
            return entity.toString();
        }
    }
}
