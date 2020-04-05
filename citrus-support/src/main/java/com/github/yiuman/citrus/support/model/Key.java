package com.github.yiuman.citrus.support.model;

/**
 * 找主键接口
 * @author yiuman
 * @date 2020/4/4
 */
public interface Key<E,K> {

    K key(E entity) throws Exception;

}