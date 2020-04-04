package com.github.yiuman.citrus.security.verify.sms;

import com.github.yiuman.citrus.security.properties.CitrusProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author yiuman
 * @date 2020/3/22
 */
public class SmsVerifyCodeSecurityConfigurerAdapter extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final CitrusProperties citrusProperties;

    private final SmsUserDetailService smsUserDetailService;

    public SmsVerifyCodeSecurityConfigurerAdapter(CitrusProperties citrusProperties, SmsUserDetailService smsUserDetailService) {
        this.citrusProperties = citrusProperties;
        this.smsUserDetailService = smsUserDetailService;
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        //构建provider
        SmsVerifyCodeAuthenticationProvider provider = new SmsVerifyCodeAuthenticationProvider(smsUserDetailService);
        //构造短信验证码Filter
        SmsVerifyCodeAuthenticationFilter filter = new SmsVerifyCodeAuthenticationFilter(citrusProperties.getSecurity().getAuthenticateEndpoint());
        filter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        http.authenticationProvider(provider).addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
