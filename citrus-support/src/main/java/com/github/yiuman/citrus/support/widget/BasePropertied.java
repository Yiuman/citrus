package com.github.yiuman.citrus.support.widget;

import cn.hutool.core.util.ReflectUtil;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @param <T> 拥有属性的对象本身
 * @author yiuman
 * @date 2022/1/23
 */
@SuppressWarnings("unchecked")
@SuperBuilder
public abstract class BasePropertied<T extends Propertied<T>> implements Propertied<T> {

    private static final Set<String> EXCLUDE_PROPERTIES_FIELD_NAME = new HashSet<String>() {{
        add("fieldToProperties");
        add("properties");
        add("EXCLUDE_PROPERTIES_FIELD_NAME");
    }};
    protected final Map<String, Object> properties = new HashMap<>();
    /**
     * 字段转到属性MAP
     */
    @Builder.Default
    private Boolean fieldToProperties = Boolean.TRUE;

    public BasePropertied() {
    }

    public void setFieldToProperties(Boolean fieldToProperties) {
        this.fieldToProperties = fieldToProperties;
    }

    @Override
    public T setProperty(String name, Object value) {
        this.properties.put(name, value);
        return (T) this;
    }

    @Override
    public Object getProperty(String name) {
        return properties.get(name);
    }

    @Override
    public boolean containsProperty(String name) {
        return properties.containsKey(name);
    }

    @Override
    public Set<String> getPropertyNames() {
        return properties.keySet();
    }

    @Override
    public Map<String, Object> getProperties() {
        if (Objects.nonNull(fieldToProperties) && fieldToProperties) {
            Field[] fields = ReflectUtil.getFields(getClass());
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    if (EXCLUDE_PROPERTIES_FIELD_NAME.contains(fieldName)
                            || Objects.isNull(field.get(this))) {
                        continue;
                    }
                    properties.put(fieldName, field.get(this));
                } catch (Throwable ignore) {
                }
            }
        }
        return properties;
    }
}
