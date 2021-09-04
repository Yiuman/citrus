package com.github.yiuman.citrus.support.crud.service;

import cn.hutool.core.util.TypeUtil;

/**
 * 实体类型接口
 *
 * @param <E> 实体类型
 * @author yiuman
 * @date 2020/4/15
 */
public interface EntityTypeService<E> {

    /**
     * 获取实体的类型
     *
     * @return 实体类型Class
     */
    @SuppressWarnings("unchecked")
    default Class<E> getEntityType() {
        return (Class<E>) TypeUtil.getTypeArgument(getClass(), 0);
    }
}