package com.github.yiuman.citrus.support.crud.rest;

import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.github.yiuman.citrus.support.crud.service.BaseService;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.utils.CrudUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 最顶层Restful基类，用于定义、实现最通用的属性与方法
 *
 * @author yiuman
 * @date 2020/10/1
 */
@Slf4j
public abstract class BaseRestful<T, K extends Serializable> {

    /**
     * 模型类型
     */
    protected Class<T> modelClass = currentModelClass();

    public BaseRestful() {
    }

    @SuppressWarnings("unchecked")
    private Class<T> currentModelClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(getClass(), 0);
    }

    /**
     * 获取CRUD逻辑层服务类
     *
     * @return 实现了 CrudService的逻辑层
     */
    @SuppressWarnings("unchecked")
    protected CrudService<T, K> getService() {
        try {
            return CrudUtils.getCrudService(
                    modelClass,
                    (Class<K>) ReflectionKit.getSuperClassGenericType(getClass(), 1),
                    BaseService.class);
        } catch (Exception e) {
            log.info("获取CrudService报错", e);
            return null;
        }

    }
}
