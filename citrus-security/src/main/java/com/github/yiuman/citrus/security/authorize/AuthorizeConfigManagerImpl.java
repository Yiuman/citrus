package com.github.yiuman.citrus.security.authorize;

import org.springframework.lang.Nullable;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

import java.util.List;

/**
 * 授权信息管理器实现
 * 用于收集系统中所有 AuthorizeConfigProvider 并加载其配置
 *
 * @author yiuman
 * @date 2020/3/23
 */
public class AuthorizeConfigManagerImpl implements AuthorizeConfigManager {

    private final List<AuthorizeConfigProvider> authorizeConfigProviders;

    private final List<WebSecurityConfigProvider> webSecurityConfigProviders;

    public AuthorizeConfigManagerImpl(List<AuthorizeConfigProvider> authorizeConfigProviders, @Nullable List<WebSecurityConfigProvider> webSecurityConfigProviders) {
        this.authorizeConfigProviders = authorizeConfigProviders;
        this.webSecurityConfigProviders = webSecurityConfigProviders;
    }


    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        boolean existAnyRequestConfig = false;
        String existAnyRequestConfigName = null;
        for (AuthorizeConfigProvider authorizeConfigProvider : authorizeConfigProviders) {
            boolean currentIsAnyRequestConfig = authorizeConfigProvider.config(config);
            if (existAnyRequestConfig && currentIsAnyRequestConfig) {
                String tips = String.format(
                        "Duplicate configuration for org.springframework.security.config.annotation.web.AbstractRequestMatcherRegistry.anyRequest:%s,%s",
                        existAnyRequestConfigName,
                        authorizeConfigProvider.getClass().getName()
                );
                throw new RuntimeException(tips);
            } else if (currentIsAnyRequestConfig) {
                existAnyRequestConfig = true;
                existAnyRequestConfigName = authorizeConfigProvider.getClass().getSimpleName();
            }
        }

        if (!existAnyRequestConfig) {
            config.anyRequest().authenticated();
        }
    }

    @Override
    public void config(WebSecurity webSecurity) {
        webSecurityConfigProviders.forEach(provider -> provider.config(webSecurity));
    }
}
