package com.github.yiuman.citrus.security.authorize;

import org.springframework.security.config.annotation.web.builders.WebSecurity;

/**
 * Web权限配置定义
 *
 * @author yiuman
 * @date 2021/7/15
 */
public interface WebSecurityConfig {

    /**
     * 配置Web安全相关，如静态资源js、html、css等
     *
     * @param webSecurity WebSecurity配置
     */
    void config(WebSecurity webSecurity);
}