package com.github.yiuman.citrus.support.crud.service;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * 基于主键的Service
 *
 * @author yiuman
 * @date 2020/4/15
 */
public interface KeyBasedService<E, K extends Serializable> extends EntityTypeService<E> {

    /**
     * 获取主键类型
     *
     * @return 主键的类型Class
     */
    @SuppressWarnings("unchecked")
    default Class<K> getKeyType() {
        return (Class<K>) ReflectionKit.getSuperClassGenericType(getClass(), 1);
    }

    /**
     * 根据实体类型获取主键
     *
     * @param entity 实体类型
     * @return 主键
     */
    @SuppressWarnings("unchecked")
    default K getKey(E entity) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(getEntityType());
        return Objects.nonNull(tableInfo) ? (K) ReflectionKit.getFieldValue(entity, tableInfo.getKeyProperty()) : null;
    }

    /**
     * 设置主键值
     *
     * @param entity 实体
     * @param key    主键
     * @throws Exception 反射相关异常
     */
    default void setKey(E entity, K key) throws Exception {
        Field field = ReflectionUtils.findField(getEntityType(), getKeyProperty());
        field.setAccessible(true);
        field.set(entity, key);
    }

    /**
     * 获取主键的属性名称
     *
     * @return 主键的属性名称 即TableId对应的Field
     */
    default String getKeyProperty() {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(getEntityType());
        return Objects.nonNull(tableInfo) ? tableInfo.getKeyProperty() : null;
    }

    /**
     * 获取当前实体的主键列名
     *
     * @return 主键列名 即TableId
     */
    default String getKeyColumn() {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(getEntityType());
        return Objects.nonNull(tableInfo) ? tableInfo.getKeyColumn() : null;
    }

}