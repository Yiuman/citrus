package com.github.yiuman.citrus.support.crud.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;

/**
 * 可删除的Service接口
 *
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

    boolean remove(Wrapper<E> wrappers);

    /**
     * 删除全部记录
     */
    void clear();

}