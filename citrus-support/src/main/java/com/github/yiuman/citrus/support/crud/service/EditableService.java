package com.github.yiuman.citrus.support.crud.service;

import org.springframework.transaction.annotation.Transactional;

/**
 * 可编辑的实体Service
 *
 * @author yiuman
 * @date 2020/4/15
 */
public interface EditableService<E, K> extends DeletableService<E, K>, SelectService<E, K> {

    /**
     * 保存实体
     *
     * @param entity 实体
     * @return 主键
     * @throws Exception 数据库异常或实体操作异常
     */
    K save(E entity) throws Exception;

    /**
     * 批量保存
     *
     * @param entityIterable 可迭代的集合
     * @return 是否保存成功 true/false
     * @throws Exception 数据库操作异常
     */
    boolean batchSave(Iterable<E> entityIterable) throws Exception;

    /**
     * 更新实体
     *
     * @param entity 实体
     * @return 主键
     * @throws Exception 数据库异常或实体操作异常
     */
    @Transactional(rollbackFor = Exception.class)
    default K update(E entity) throws Exception {
        return save(entity);
    }
}