package com.github.yiuman.citrus.support.crud.query.annotations;

import com.github.yiuman.citrus.support.crud.query.QueryParamHandler;
import com.github.yiuman.citrus.support.crud.query.impl.DefaultQueryParamHandler;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SQL `=`操作注解
 *
 * @author yiuman
 * @date 2021/6/13
 */
@QueryParam
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Equals {

    @AliasFor(annotation = QueryParam.class)
    String mapping() default "";

    /**
     * 为空时是否拼接条件
     *
     * @return true/false
     */
    @AliasFor(annotation = QueryParam.class)
    boolean condition() default true;

    /**
     * 查询参数处理器
     *
     * @return 查询参数处理器的类型，实现QueryParamHandler,处理查询条件
     */
    @AliasFor(annotation = QueryParam.class)
    Class<? extends QueryParamHandler> handler() default DefaultQueryParamHandler.class;
}