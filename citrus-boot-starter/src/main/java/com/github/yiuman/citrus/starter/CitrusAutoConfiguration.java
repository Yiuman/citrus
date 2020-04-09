package com.github.yiuman.citrus.starter;

import com.github.yiuman.citrus.security.authorize.AuthorizeConfigManager;
import com.github.yiuman.citrus.security.jwt.JwtSecurityConfigurerAdapter;
import com.github.yiuman.citrus.security.properties.CitrusProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自动配置
 *
 * @author yiuman
 * @date 2020/3/22
 */
@Configuration
@ConditionalOnBean(EnableCitrusAdmin.class)
@EnableConfigurationProperties(CitrusProperties.class)
@EnableAutoConfiguration(exclude = {UserDetailsServiceAutoConfiguration.class})
public class CitrusAutoConfiguration {

    /**
     * 无状态下的Security配置
     */
    @Configuration
    @ConditionalOnProperty(prefix = "citrus.security", name = "stateless", havingValue = "true", matchIfMissing = true)
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
    public static class StatelessSecurityConfiguration extends WebSecurityConfigurerAdapter {

        private final AuthenticationEntryPoint authenticationEntryPoint;

        private final JwtSecurityConfigurerAdapter jwtSecurityConfigurerAdapter;

        private final AuthorizeConfigManager authorizeConfigManager;

        public StatelessSecurityConfiguration(AuthenticationEntryPoint authenticationEntryPoint, JwtSecurityConfigurerAdapter jwtSecurityConfigurerAdapter, AuthorizeConfigManager authorizeConfigManager) {
            this.authenticationEntryPoint = authenticationEntryPoint;
            this.jwtSecurityConfigurerAdapter = jwtSecurityConfigurerAdapter;
            this.authorizeConfigManager = authorizeConfigManager;
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
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .apply(jwtSecurityConfigurerAdapter);
            authorizeConfigManager.config(http.authorizeRequests());
        }


    }


    /**
     * 有状态的Security配置
     */
    @Configuration
    @ConditionalOnProperty(prefix = "citrus.security", name = "stateless", havingValue = "false")
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
    private static class StatefulSecurityConfiguration extends WebSecurityConfigurerAdapter {


    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
    public AuthenticationEntryPoint entryPoint() {
        return new UnAuthenticationEntryPoint();
    }

    @Component
    private static class UnAuthenticationEntryPoint implements AuthenticationEntryPoint {

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        }
    }

}
