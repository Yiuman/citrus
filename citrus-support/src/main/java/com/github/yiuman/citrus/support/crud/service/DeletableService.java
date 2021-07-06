package com.github.yiuman.citrus.support.crud.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = Exception.class)
    boolean remove(E entity) throws Exception;

    /**
     * 批量删除
     *
     * @param keys 数据主键集合
     * @throws Exception 一般为数据库操作异常，或实体操作过程中的异常
     */
    @Transactional(rollbackFor = Exception.class)
    void batchRemove(Iterable<K> keys) throws Exception;

    /**
     * 删除
     *
     * @param wrappers 删除的条件
     * @return 是否删除成功true/false
     * @throws Exception 一般为数据库操作异常，或实体操作过程中的异常
     */
    @Transactional(rollbackFor = Exception.class)
    boolean remove(Wrapper<E> wrappers) throws Exception;

    /**
     * 删除全部记录
     *
     * @throws Exception 一般为数据库操作异常，或实体操作过程中的异常
     */
    @Transactional(rollbackFor = Exception.class)
    void clear() throws Exception;

}