package com.github.yiuman.citrus.support.crud;

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

    String type() default "eq";

    boolean condition() default true;

    Class<? extends QueryParamHandler> handler() default QueryParamHandler.class;
}