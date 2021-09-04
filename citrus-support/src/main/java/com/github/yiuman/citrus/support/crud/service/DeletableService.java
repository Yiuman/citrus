package com.github.yiuman.citrus.support.crud.service;

import com.github.yiuman.citrus.support.crud.query.Query;

/**
 * 可删除的Service接口
 *
 * @param <E> 实体类型
 * @param <K> 主键类型
 * @author yiuman
 * @date 2020/4/15
 */
public interface DeletableService<E, K> {

    /**
     * 删除实体
     *
     * @param entity 实体
     * @return 是否删除成功 true/false
     * @throws Exception 一般为数据库操作异常，或实体操作过程中的异常
     */
    boolean remove(E entity) throws Exception;

    /**
     * 批量删除
     *
     * @param keys 数据主键集合
     * @throws Exception 一般为数据库操作异常，或实体操作过程中的异常
     */
    void batchRemove(Iterable<K> keys) throws Exception;

    /**
     * 删除
     *
     * @param query 删除的条件
     * @return 是否删除成功true/false
     * @throws Exception 一般为数据库操作异常，或实体操作过程中的异常
     */
    boolean remove(Query query) throws Exception;

    /**
     * 删除全部记录
     *
     * @throws Exception 一般为数据库操作异常，或实体操作过程中的异常
     */
    void clear() throws Exception;

}