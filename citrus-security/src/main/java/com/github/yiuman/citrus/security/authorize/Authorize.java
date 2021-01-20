package com.github.yiuman.citrus.security.authorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 鉴权注解，AOP自定义校验
 *
 * @author yiuman
 * @date 2020/4/4
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorize {

    /**
     * 权限钩子实现，用于AOP执行hasPermission方法鉴权
     */
    Class<? extends AuthorizeHook> value();
}