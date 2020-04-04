package com.github.yiuman.citrus.security.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author yiuman
 * @date 2020/3/22
 */
public interface JwtProvider {

    /**
     * 构造token
     * @param authentication 当前身份认证信息
     * @return JWT
     */
    String generateToken(Authentication authentication, Map<String, Object> claims);

    /**
     * 检验TOKEN
     */
    boolean validateToken(String token);

    /**
     * 解析token
     */
    String resolveToken(HttpServletRequest request);

    Claims getClaims(String token);

    String getIdentityKey();
}