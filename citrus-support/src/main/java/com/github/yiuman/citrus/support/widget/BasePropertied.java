package com.github.yiuman.citrus.support.widget;

import cn.hutool.core.util.ReflectUtil;
import lombok.experimental.SuperBuilder;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @param <T> 拥有属性的对象本身
 * @author yiuman
 * @date 2022/1/23
 */
@SuppressWarnings("unchecked")
@SuperBuilder
public abstract class BasePropertied<T extends Propertied<T>> implements Propertied<T> {

    private static final String FIELD_TO_PROPERTIES_FIELD_NAME = "fieldToProperties";
    private static final String PROPERTIES_FIELD_NAME = "properties";

    /**
     * 字段转到属性MAP
     */
    private Boolean fieldToProperties = Boolean.TRUE;

    protected Map<String, Object> properties = new HashMap<>();

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
        if (fieldToProperties) {
            Field[] fields = ReflectUtil.getFields(getClass());
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    if (fieldName.equals(FIELD_TO_PROPERTIES_FIELD_NAME)
                            || fieldName.equals(PROPERTIES_FIELD_NAME)
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
