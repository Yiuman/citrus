package com.github.yiuman.citrus.security.authorize;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * 授权配置管理器
 *
 * @author yiuman
 * @date 2021/7/15
 */
public interface AuthorizeConfigManager extends WebSecurityConfig {

    /**
     * 配置Http安全相关
     *
     * @param config HttpSecurity配置
     */
    void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config);

}