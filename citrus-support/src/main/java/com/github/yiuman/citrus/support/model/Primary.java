package com.github.yiuman.citrus.support.model;

import java.lang.annotation.*;

/**
 * 标记主键
 * @author yiuman
 * @date 2020/4/4
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Primary {
}