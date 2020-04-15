package com.github.yiuman.citrus.support.crud.service;

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
     */
    K save(E entity) throws Exception;

    /**
     * 批量保存
     *
     * @param entityIterable 可迭代的集合
     * @return 是否保存成功 true/false
     */
    boolean batchSave(Iterable<E> entityIterable);

    /**
     * 更新实体
     *
     * @param entity 实体
     * @return 主键
     */
    default K update(E entity) throws Exception {
        return this.save(entity);
    }
}