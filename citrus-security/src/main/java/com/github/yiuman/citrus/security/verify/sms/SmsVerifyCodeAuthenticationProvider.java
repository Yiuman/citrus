package com.github.yiuman.citrus.security.verify.sms;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 短信验证码认证Provider
 *
 * @author yiuman
 * @date 2020/3/22
 */
public class SmsVerifyCodeAuthenticationProvider implements AuthenticationProvider {

    private final SmsUserDetailService smsUserDetailService;

    public SmsVerifyCodeAuthenticationProvider(SmsUserDetailService smsUserDetailService) {
        this.smsUserDetailService = smsUserDetailService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsVerifyCodeAuthenticationToken authenticationToken = (SmsVerifyCodeAuthenticationToken) authentication;
        UserDetails userDetails = smsUserDetailService.loadUserByMobile((String) authenticationToken.getPrincipal());
        if (userDetails == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }

        SmsVerifyCodeAuthenticationToken result = new SmsVerifyCodeAuthenticationToken(userDetails.getAuthorities(), userDetails);
        result.setDetails(authenticationToken.getDetails());
        return result;

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsVerifyCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
