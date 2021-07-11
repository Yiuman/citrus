package com.github.yiuman.citrus.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yiuman.citrus.support.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证异常处理，用于处理认真失败异常
 *
 * @author yiuman
 * @date 2020/6/20
 */
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
    	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(ResponseEntity.error(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage())));
    }
}
