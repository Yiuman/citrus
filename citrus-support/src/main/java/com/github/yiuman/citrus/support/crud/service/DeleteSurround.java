package com.github.yiuman.citrus.support.crud.service;

/**
 * 基于实体的删除操作环绕
 *
 * @author yiuman
 * @date 2021/7/6
 * @see DeletableService
 */
public interface DeleteSurround<E> {

    /**
     * 删除前的操作
     *
     * @param entity 当前实体对象
     * @return true/false true表示执行可下步操作
     */
    default boolean beforeRemove(E entity) {
        return true;
    }

}