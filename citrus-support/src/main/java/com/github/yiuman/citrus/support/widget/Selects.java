package com.github.yiuman.citrus.support.widget;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 用于定义选择器
 *
 * @author yiuman
 * @date 2020/5/7
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Selects {

    boolean multiple() default false;

    String bind();

    String key();

    String text() default "";

    String label() default "";

}