package com.github.yiuman.citrus.security.verify;

import com.github.yiuman.citrus.security.properties.SecurityProperties;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 验证过滤器
 *
 * @author yiuman
 * @date 2020/3/22
 */
public class VerificationFilter extends OncePerRequestFilter {

    private final SecurityProperties securityProperties;

    private final List<VerificationProcessor<?>> verificationProcessors;

    public VerificationFilter(SecurityProperties securityProperties, List<VerificationProcessor<?>> verificationProcessors) {
        this.securityProperties = securityProperties;
        this.verificationProcessors = verificationProcessors;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String verificationType = verificationType(request);
        if (Strings.isNotBlank(verificationType)) {
            VerificationProcessor<?> processor = verificationProcessors.stream()
                    .filter(verificationProcessor -> verificationType.equals(verificationProcessor.verificationType()))
                    .findFirst()
                    .orElseThrow(() -> new VerificationException("错误的验证类型"));
            processor.validate(request);
        }

        filterChain.doFilter(request, response);
    }

    private String verificationType(HttpServletRequest request) {
        return request.getParameter(securityProperties.getLoginTypeName());
    }
}
