package com.github.yiuman.citrus.support.model;

/**
 * 找主键接口
 *
 * @author yiuman
 * @date 2020/4/4
 */
public interface Key<E, K> {

    /**
     * 获取实体主键
     *
     * @param entity 当前实体
     * @return 实体的主键
     * @throws Exception 一般为反射异常
     */
    K key(E entity) throws Exception;

}