package com.github.yiuman.citrus.support.crud.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;

/**
 * 查询参数处理器
 *
 * @author yiuman
 * @date 2020/4/5
 */
public interface QueryParamHandler {

    /**
     * 根据@QueryParam注解的定义构造查询条件
     *
     * @param queryParam   查询注解
     * @param object       当前查询参数对象
     * @param field        当前这个查询参数对象的需要处理的属性
     * @param queryWrapper mybatis-plus查询构造器
     * @throws Exception 大多为反射异常
     */
    void handle(QueryParam queryParam, Object object, Field field, QueryWrapper<?> queryWrapper) throws Exception;
}