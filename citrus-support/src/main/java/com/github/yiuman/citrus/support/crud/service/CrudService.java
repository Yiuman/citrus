package com.github.yiuman.citrus.support.crud.service;

import java.io.Serializable;

/**
 * 实体增删改查Service
 *
 * @author yiuman
 * @date 2020/4/15
 */
public interface CrudService<E, K extends Serializable> extends EditableService<E, K>, KeyBasedService<E, K> {

    /**
     * 保存前的操作
     *
     * @param entity 实体
     * @return 若为true则继续执行下面的操作，否则不执行
     */
    default boolean beforeSave(E entity) throws Exception {
        return true;
    }

    /**
     * 保存后的操作
     *
     * @param entity 实体
     */
    default void afterSave(E entity) {

    }

    /**
     * 更新前的操作
     *
     * @param entity 实体
     * @return 若为true则继续执行下面的操作，否则不执行
     */
    default boolean beforeUpdate(E entity) {
        return true;
    }

    /**
     * 更新后的操作
     *
     * @param entity 实体
     */
    default void afterUpdate(E entity) {

    }

    /**
     * 删除前的操作
     *
     * @param entity 实体
     * @return 若为true则继续执行下面的操作，否则不执行
     */
    default boolean beforeRemove(E entity) {
        return true;
    }
}