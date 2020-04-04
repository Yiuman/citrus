package com.github.yiuman.citrus.security.verify;

import com.github.yiuman.citrus.security.properties.CitrusProperties;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yiuman
 * @date 2020/3/23
 */
@Component
public class VerifySecurityConfigurerAdapter extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final List<VerificationProcessor<?>> processorList;

    private final CitrusProperties citrusProperties;

    public VerifySecurityConfigurerAdapter(List<VerificationProcessor<?>> processorList, CitrusProperties citrusProperties) {
        this.processorList = processorList;
        this.citrusProperties = citrusProperties;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //往Spring Security中表单认证filter前添加验证码检验的filter
        http.addFilterBefore(new VerificationFilter(citrusProperties.getSecurity(), processorList), UsernamePasswordAuthenticationFilter.class);
    }
}
