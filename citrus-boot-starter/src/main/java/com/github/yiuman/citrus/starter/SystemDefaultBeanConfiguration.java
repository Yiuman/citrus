package com.github.yiuman.citrus.starter;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.yiuman.citrus.security.authenticate.AuthenticateProcessor;
import com.github.yiuman.citrus.security.authenticate.AuthenticateProcessorImpl;
import com.github.yiuman.citrus.security.authenticate.AuthenticateService;
import com.github.yiuman.citrus.security.authorize.AuthorizeConfigManager;
import com.github.yiuman.citrus.security.authorize.AuthorizeConfigManagerImpl;
import com.github.yiuman.citrus.security.authorize.AuthorizeConfigProvider;
import com.github.yiuman.citrus.security.authorize.WebSecurityConfigProvider;
import com.github.yiuman.citrus.security.jwt.JwtAccessDeniedHandler;
import com.github.yiuman.citrus.security.jwt.JwtAuthenticationEntryPoint;
import com.github.yiuman.citrus.security.jwt.JwtAuthenticationFilter;
import com.github.yiuman.citrus.security.jwt.JwtSecurityConfigurerAdapter;
import com.github.yiuman.citrus.support.file.FileStorageService;
import com.github.yiuman.citrus.support.file.LocalFileStorageServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.util.List;

/**
 * 系统默认实例配置
 *
 * @author yiuman
 * @date 2020/3/23
 */
@Configuration
public class SystemDefaultBeanConfiguration {

    @Bean
    @Primary
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper jacksonObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        //用于解决java.time 模块的时间序列化为json时变成数组的问题
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        return objectMapper;

    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
    public AuthenticationEntryPoint entryPoint() {
        return new JwtAuthenticationEntryPoint(jacksonObjectMapper());
    }

    @Bean
    @ConditionalOnMissingBean(AccessDeniedHandler.class)
    public AccessDeniedHandler accessDeniedHandler() {
        return new JwtAccessDeniedHandler(jacksonObjectMapper());
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticateProcessor.class)
    public AuthenticateProcessor authenticateProcessor(List<AuthenticateService> authenticateServices) {
        return new AuthenticateProcessorImpl(authenticateServices);
    }

    @Bean
    @ConditionalOnMissingBean(JwtSecurityConfigurerAdapter.class)
    public JwtSecurityConfigurerAdapter jwtSecurityConfigurerAdapter(AuthenticateProcessor authenticateProcessor) {
        return new JwtSecurityConfigurerAdapter(new JwtAuthenticationFilter(authenticateProcessor));
    }


    /**
     * 创建授权配置管理器
     *
     * @param authorizeConfigProviders Spring Security授权配置提供者集合
     * @return 授权配置管理器
     */
    @Bean
    @ConditionalOnMissingBean(AuthorizeConfigManagerImpl.class)
    public AuthorizeConfigManager authorizeConfigManager(List<AuthorizeConfigProvider> authorizeConfigProviders, List<WebSecurityConfigProvider> webSecurityConfigProviders) {
        return new AuthorizeConfigManagerImpl(authorizeConfigProviders, webSecurityConfigProviders);
    }

    /**
     * 默认加密
     */
    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        // 密码加密方式
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean(MybatisPlusInterceptor.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(paginationInterceptor());
        mybatisPlusInterceptor.addInnerInterceptor(optimisticLockerInterceptor());
        return mybatisPlusInterceptor;
    }

    @Bean
    @ConditionalOnMissingBean(PaginationInnerInterceptor.class)
    public PaginationInnerInterceptor paginationInterceptor() {
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        paginationInterceptor.setMaxLimit(500L);
        return paginationInterceptor;
    }

    @Bean
    @ConditionalOnMissingBean(OptimisticLockerInnerInterceptor.class)
    public OptimisticLockerInnerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInnerInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean(RedisTemplate.class)
    public RedisTemplate<?, ?> getRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<?, ?> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.afterPropertiesSet();
        return template;
    }


    /**
     * 默认的文件存储服务类，本地路径classpath:
     *
     * @return 文件存储服务类
     */
    @Bean
    @ConditionalOnMissingBean(FileStorageService.class)
    public FileStorageService getFileStorageService() {
        return new LocalFileStorageServiceImpl();
    }


}
