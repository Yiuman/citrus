package com.github.yiuman.citrus.starter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.yiuman.citrus.security.authenticate.AuthenticateProcessor;
import com.github.yiuman.citrus.security.authenticate.AuthenticateProcessorImpl;
import com.github.yiuman.citrus.security.authenticate.AuthenticateService;
import com.github.yiuman.citrus.security.authorize.AuthorizeConfigManager;
import com.github.yiuman.citrus.security.authorize.AuthorizeConfigProvider;
import com.github.yiuman.citrus.security.jwt.JwtAccessDeniedHandler;
import com.github.yiuman.citrus.security.jwt.JwtAuthenticationEntryPoint;
import com.github.yiuman.citrus.security.jwt.JwtAuthenticationFilter;
import com.github.yiuman.citrus.security.jwt.JwtSecurityConfigurerAdapter;
import com.github.yiuman.citrus.security.properties.CitrusProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.util.List;

/**
 * 自动配置
 *
 * @author yiuman
 * @date 2020/3/22
 */
@Configuration
@EnableConfigurationProperties(CitrusProperties.class)
@ConditionalOnClass(WebSecurityConfigurerAdapter.class)
@ConditionalOnMissingBean(WebSecurityConfigurerAdapter.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ComponentScans({
        @ComponentScan("com.github.yiuman.citrus.support"),
        @ComponentScan("com.github.yiuman.citrus.security"),
        @ComponentScan("com.github.yiuman.citrus.system")
})
@MapperScan(basePackages = "com.github.yiuman.citrus.system.mapper")
@Import({SystemDefaultBeanConfiguration.class, VerifyConfiguration.class})
@EnableWebSecurity
public class CitrusAutoConfiguration {

    /**
     * 无状态下的Security配置
     */
    @Configuration
    @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
    public static class StatelessSecurityConfiguration extends WebSecurityConfigurerAdapter {

        private final AuthenticationEntryPoint authenticationEntryPoint;

        private final AccessDeniedHandler accessDeniedHandler;

        private final JwtSecurityConfigurerAdapter jwtSecurityConfigurerAdapter;

        private final AuthorizeConfigManager authorizeConfigManager;

        public StatelessSecurityConfiguration(AuthenticationEntryPoint authenticationEntryPoint, JwtSecurityConfigurerAdapter jwtSecurityConfigurerAdapter, AuthorizeConfigManager authorizeConfigManager, AccessDeniedHandler accessDeniedHandler) {
            this.authenticationEntryPoint = authenticationEntryPoint;
            this.jwtSecurityConfigurerAdapter = jwtSecurityConfigurerAdapter;
            this.authorizeConfigManager = authorizeConfigManager;
            this.accessDeniedHandler = accessDeniedHandler;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    // 禁用 CSRF
                    .csrf()
                    .disable()
                    .formLogin()
                    .disable()
                    .exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler)
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .apply(jwtSecurityConfigurerAdapter);

            authorizeConfigManager.config(http.authorizeRequests());
        }

    }

    @Bean
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        //用于解决java.time 模块的时间序列化为json时变成数组的问题
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;

    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
    public AuthenticationEntryPoint entryPoint() {
        return new JwtAuthenticationEntryPoint(objectMapper());
    }

    @Bean
    @ConditionalOnMissingBean(AccessDeniedHandler.class)
    public AccessDeniedHandler accessDeniedHandler() {
        return new JwtAccessDeniedHandler(objectMapper());
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


    @Bean
    @ConditionalOnMissingBean(AuthorizeConfigManager.class)
    public AuthorizeConfigManager authorizeConfigManager(List<AuthorizeConfigProvider> authorizeConfigProviders) {
        return new AuthorizeConfigManager(authorizeConfigProviders);

    }


}
