package com.github.yiuman.citrus.system.config;

import com.github.yiuman.citrus.system.hook.AccessPointer;
import com.github.yiuman.citrus.system.hook.DefaultAccessPointerImpl;
import com.github.yiuman.citrus.system.service.AccessLogService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 系统Bean实例配置
 *
 * @author yiuman
 * @date 2021/7/15
 */
@Configuration
public class SystemBeanConfig {

    public SystemBeanConfig() {
    }

    @Bean
    @ConditionalOnMissingBean(AccessPointer.class)
    public AccessPointer accessPointer(AccessLogService accessLogService) {
        return new DefaultAccessPointerImpl(accessLogService);
    }
}
