package com.github.yiuman.citrus.support.crud.service;

import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;

/**
 * 实体类型接口
 *
 * @author yiuman
 * @date 2020/4/15
 */
public interface EntityTypeService<E> {

    @SuppressWarnings("unchecked")
    default Class<E> getEntityType() {
        return (Class<E>) ReflectionKit.getSuperClassGenericType(getClass(), 0);
    }
}