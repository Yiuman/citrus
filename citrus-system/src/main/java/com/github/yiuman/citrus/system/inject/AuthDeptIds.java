package com.github.yiuman.citrus.system.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据部门范围注入注解
 *
 * @author yiuman
 * @date 2020/7/23
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthDeptIds {

    String code() default "";
}