package com.github.yiuman.citrus.support.crud.service;

import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.github.yiuman.citrus.support.cache.InMemoryCache;
import com.github.yiuman.citrus.support.utils.CacheUtils;
import com.github.yiuman.citrus.support.utils.CrudUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author yiuman
 * @date 2021/7/8
 */
@Slf4j
public final class CrudHelper {

    /**
     * 字节码动态CrudService缓存
     */
    private final static InMemoryCache<Class<?>, CrudService<?, ? extends Serializable>> SERVICE_CACHE = CacheUtils.newInMemoryCache("SERVICE_CACHE");

    private CrudHelper() {
    }

    @SuppressWarnings("unchecked")
    public static <E, K extends Serializable, S extends CrudService<E, K>> S getCrudService(
            Class<E> entityClass,
            Class<K> keyClass) {
        CrudService<?, ? extends Serializable> crudService = SERVICE_CACHE.find(entityClass);
        if (Objects.nonNull(crudService)) {
            return (S) crudService;
        }
        try {
            crudService = CrudUtils.getCrudService(entityClass, keyClass, BaseService.class);
        } catch (Exception e) {
            log.error("Cannot auto create baseService for entity {}", ReflectionKit.getSuperClassGenericType(entityClass, 0), e);
            throw new RuntimeException(e);
        }
        SERVICE_CACHE.save(entityClass, crudService);
        return (S) crudService;
    }

    @SuppressWarnings("unchecked")
    public static <E, K extends Serializable, S extends CrudService<E, K>> S getCrudService(Class<?> genericsClass) {
        return getCrudService(
                (Class<E>) ReflectionKit.getSuperClassGenericType(genericsClass, 0)
                , (Class<K>) ReflectionKit.getSuperClassGenericType(genericsClass, 1)
        );
    }
}
