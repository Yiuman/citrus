package com.github.yiuman.citrus.support.crud.service;

/**
 * 编辑环绕
 *
 * @author yiuman
 * @date 2021/7/6
 */
public interface EditSurround<E> {

    /**
     * 保存前的操作
     *
     * @param entity 实体
     * @return 若为true则继续执行下面的操作，否则不执行
     * @throws Exception 可能为数据库操作异常或其他异常
     */
    default boolean beforeSave(E entity) throws Exception {
        return true;
    }

    /**
     * 保存后的操作
     *
     * @param entity 实体
     * @throws Exception 数据库操作异常
     */
    default void afterSave(E entity) throws Exception {

    }

    /**
     * 批量保存操作前的操作
     *
     * @param entities 实例迭代
     * @return true/false true表示可执行下步操作
     */
    default boolean beforeBatchSave(Iterable<E> entities) {
        return true;
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
}