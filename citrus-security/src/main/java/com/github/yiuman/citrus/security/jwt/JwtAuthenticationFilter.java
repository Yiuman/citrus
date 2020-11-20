package com.github.yiuman.citrus.security.jwt;

import com.github.yiuman.citrus.security.authenticate.AuthenticateProcessor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT认证过滤器
 *
 * @author yiuman
 * @date 2020/3/22
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticateProcessor authenticateProcessor;

    public JwtAuthenticationFilter(AuthenticateProcessor authenticateProcessor) {
        this.authenticateProcessor = authenticateProcessor;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        authenticateProcessor
                .resolve(request)
                .ifPresent(authentication -> SecurityContextHolder.getContext().setAuthentication(authentication));
        filterChain.doFilter(request, response);
    }


}
