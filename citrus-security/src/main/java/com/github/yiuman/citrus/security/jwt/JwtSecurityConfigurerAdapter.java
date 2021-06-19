package com.github.yiuman.citrus.security.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * Jwt安全配置适配
 *
 * @author yiuman
 * @date 2020/3/22
 */
@Component
public class JwtSecurityConfigurerAdapter extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtAuthenticationFilter filter;

    public JwtSecurityConfigurerAdapter(JwtAuthenticationFilter filter) {
        this.filter = filter;
    }

    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
