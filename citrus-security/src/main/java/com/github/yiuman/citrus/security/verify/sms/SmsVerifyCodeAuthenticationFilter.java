package com.github.yiuman.citrus.security.verify.sms;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 短信验证码登录认证过滤器
 * 参考UsernamePasswordAuthenticationFilter编写
 *
 * @author yiuman
 * @date 2020/3/22
 */
public class SmsVerifyCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public SmsVerifyCodeAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl, "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        boolean postOnly = true;
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String mobileParameter = "mobile";
        String mobile = request.getParameter(mobileParameter);

        if (mobile == null) {
            mobile = "";
        }

        mobile = mobile.trim();

        SmsVerifyCodeAuthenticationToken smsCodeAuthenticationToken = new SmsVerifyCodeAuthenticationToken(mobile);

        // Allow subclasses to set the "details" property
        setDetails(request, smsCodeAuthenticationToken);

        return this.getAuthenticationManager().authenticate(smsCodeAuthenticationToken);

    }

    protected void setDetails(HttpServletRequest request, SmsVerifyCodeAuthenticationToken smsCodeAuthenticationToken) {
        smsCodeAuthenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));
    }
}
