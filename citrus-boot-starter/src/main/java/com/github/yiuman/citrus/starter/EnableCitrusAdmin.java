package com.github.yiuman.citrus.starter;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启Citrus-Admin
 *
 * @author yiuman
 * @date 2020/3/22
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(CitrusAutoConfiguration.class)
public @interface EnableCitrusAdmin {
}