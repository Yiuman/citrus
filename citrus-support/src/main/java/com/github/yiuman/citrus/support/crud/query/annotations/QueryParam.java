package com.github.yiuman.citrus.support.crud.query.annotations;

import com.github.yiuman.citrus.support.crud.query.QueryParamHandler;
import com.github.yiuman.citrus.support.crud.query.impl.DefaultQueryParamHandler;

import java.lang.annotation.*;

/**
 * 查询参数注解，用于列表查询，默认为eq
 *
 * @author yiuman
 * @date 2020/4/5
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryParam {

    /**
     * 参数处理类型
     *
     * @return 对应mybatis-plus中的wrapper类型
     */
    String type() default "eq";

    /**
     * 用于映射查询名称
     * 比如此字段是userId   映射到表的是user_id 此值为user_id
     *
     * @return 映射的查询名称
     */
    String mapping() default "";

    /**
     * 为空时是否拼接条件
     *
     * @return true/false
     */
    boolean condition() default true;

    /**
     * 查询参数处理器
     *
     * @return 查询参数处理器的类型，实现QueryParamHandler,处理查询条件
     */
    Class<? extends QueryParamHandler> handler() default DefaultQueryParamHandler.class;

}