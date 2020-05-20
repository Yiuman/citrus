package com.github.yiuman.citrus.support.crud.service;

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
     */
    boolean remove(E entity) throws Exception;

    /**
     * 批量删除
     *
     * @param keys 数据主键集合
     */
    void batchRemove(Iterable<K> keys) throws Exception;

    /**
     * 删除全部记录
     *
     */
    void clear();

}