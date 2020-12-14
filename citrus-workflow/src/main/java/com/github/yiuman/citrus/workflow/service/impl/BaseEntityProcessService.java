package com.github.yiuman.citrus.workflow.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.github.yiuman.citrus.support.crud.service.BaseService;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.utils.CrudUtils;
import com.github.yiuman.citrus.workflow.model.ProcessBusinessModel;
import com.github.yiuman.citrus.workflow.service.BaseProcessService;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @author yiuman
 * @date 2020/12/14
 */
@Slf4j
public abstract class BaseEntityProcessService<E extends ProcessBusinessModel, K extends Serializable>
        extends BaseProcessService implements CrudService<E, K> {

    @SuppressWarnings("unchecked")
    protected Class<E> entityClass = (Class<E>) getSuperClassGenericType(0);

    @SuppressWarnings("unchecked")
    protected Class<K> keyClass = (Class<K>) getSuperClassGenericType(1);

    public BaseEntityProcessService() {
    }

    private Class<?> getSuperClassGenericType(int index) {
        return ReflectionKit.getSuperClassGenericType(getClass(), index);
    }

    @SuppressWarnings("unchecked")
    protected CrudService<E, K> getCurdService() {
        try {
            return CrudUtils.getCrudService(
                    entityClass,
                    keyClass,
                    BaseService.class);
        } catch (Exception e) {
            log.info("获取CrudService报错", e);
            return null;
        }
    }

    @Override
    public boolean beforeSave(E entity) throws Exception {
        return getCurdService().beforeSave(entity);
    }

    @Override
    public void afterSave(E entity) throws Exception {
        getCurdService().afterSave(entity);
    }

    @Override
    public boolean beforeUpdate(E entity) {
        return getCurdService().beforeUpdate(entity);
    }

    @Override
    public void afterUpdate(E entity) {
        getCurdService().afterUpdate(entity);
    }

    @Override
    public boolean beforeRemove(E entity) {
        return getCurdService().beforeRemove(entity);
    }

    @Override
    public K save(E entity) throws Exception {
        return getCurdService().save(entity);
    }

    @Override
    public boolean batchSave(Iterable<E> entityIterable) {
        return getCurdService().batchSave(entityIterable);
    }

    @Override
    public K update(E entity) throws Exception {
        return getCurdService().update(entity);
    }

    @Override
    public Class<K> getKeyType() {
        return getCurdService().getKeyType();
    }

    @Override
    public K getKey(E entity) {
        return getCurdService().getKey(entity);
    }

    @Override
    public void setKey(E entity, K key) throws Exception {
        getCurdService().setKey(entity, key);
    }

    @Override
    public String getKeyProperty() {
        return getCurdService().getKeyProperty();
    }

    @Override
    public String getKeyColumn() {
        return getCurdService().getKeyColumn();
    }

    @Override
    public Class<E> getEntityType() {
        return getCurdService().getEntityType();
    }
}
