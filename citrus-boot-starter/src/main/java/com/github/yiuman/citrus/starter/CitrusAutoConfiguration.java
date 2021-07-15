package com.github.yiuman.citrus.starter;

import com.github.yiuman.citrus.security.authorize.AuthorizeConfigManager;
import com.github.yiuman.citrus.security.jwt.JwtSecurityConfigurerAdapter;
import com.github.yiuman.citrus.security.properties.CitrusProperties;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * 自动配置
 *
 * @author yiuman
 * @date 2020/3/22
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(WebSecurityConfigurerAdapter.class)
@ConditionalOnMissingBean(WebSecurityConfigurerAdapter.class)
@EnableConfigurationProperties(CitrusProperties.class)
@AutoConfigureBefore({JacksonAutoConfiguration.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ComponentScans({
        @ComponentScan("com.github.yiuman.citrus.support"),
        @ComponentScan("com.github.yiuman.citrus.security"),
        @ComponentScan("com.github.yiuman.citrus.system"),
})
@MapperScan(basePackages = "com.github.yiuman.citrus.system.mapper")
@Import({SystemDefaultBeanConfiguration.class, VerifyConfiguration.class})
@EnableWebSecurity
@Slf4j
public class CitrusAutoConfiguration {

    /**
     * 无状态下的Security配置
     */
    @Configuration
    @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
    public static class SecurityConfiguration extends WebSecurityConfigurerAdapter {

        private final AuthenticationEntryPoint authenticationEntryPoint;

        private final AccessDeniedHandler accessDeniedHandler;

        /**
         * JWT安全配置适配器，主要是将过滤器放入Security过滤链
         */
        private final JwtSecurityConfigurerAdapter jwtSecurityConfigurerAdapter;

        /**
         * 授权配置管理器，用与解耦业务与Security的安全配置
         */
        private final AuthorizeConfigManager authorizeConfigManager;

        public SecurityConfiguration(AuthenticationEntryPoint authenticationEntryPoint, JwtSecurityConfigurerAdapter jwtSecurityConfigurerAdapter, AuthorizeConfigManager authorizeConfigManager, AccessDeniedHandler accessDeniedHandler) {
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

        @Override
        public void configure(WebSecurity web) {
            authorizeConfigManager.config(web);
        }
    }

}
