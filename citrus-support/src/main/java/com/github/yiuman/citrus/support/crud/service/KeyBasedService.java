package com.github.yiuman.citrus.support.crud.service;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;

import java.io.Serializable;

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
        return (K) ReflectionKit.getMethodValue(getEntityType(), entity, tableInfo.getKeyProperty());
    }

    /**
     * 获取主键的属性名称
     *
     * @return 主键的属性名称 即TableId对应的Field
     */
    default String getKeyProperty() {
        return TableInfoHelper.getTableInfo(getEntityType()).getKeyProperty();
    }

    /**
     * 获取当前实体的主键列名
     *
     * @return 主键列名 即TableId
     */
    default String getKeyColumn() {
        return TableInfoHelper.getTableInfo(getEntityType()).getKeyColumn();
    }

}