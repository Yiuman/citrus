package com.github.yiuman.citrus.support.crud.rest;

import cn.hutool.core.util.TypeUtil;
import com.github.yiuman.citrus.support.crud.CrudHelper;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 最顶层Restful基类，用于定义、实现最通用的属性与方法
 *
 * @param <T> 实体类型
 * @param <K> 主键类型
 * @author yiuman
 * @date 2020/10/1
 */
@Slf4j
public abstract class BaseRestful<T, K extends Serializable> {

    /**
     * 模型类型
     */
    protected Class<T> modelClass = currentModelClass();

    protected Class<K> keyClass = currentKeyClass();

    public BaseRestful() {
    }

    @SuppressWarnings("unchecked")
    private Class<T> currentModelClass() {
        return (Class<T>) TypeUtil.getTypeArgument(getClass(), 0);
    }

    @SuppressWarnings("unchecked")
    private Class<K> currentKeyClass() {
        return (Class<K>) TypeUtil.getTypeArgument(getClass(), 1);
    }

    /**
     * 获取CRUD逻辑层服务类
     *
     * @return 实现了 CrudService的逻辑层
     */
    protected CrudService<T, K> getService() {
        return CrudHelper.getCrudService(modelClass, keyClass);
    }

    public Class<T> getModelClass() {
        return modelClass;
    }

    public Class<K> getKeyClass() {
        return keyClass;
    }
}
