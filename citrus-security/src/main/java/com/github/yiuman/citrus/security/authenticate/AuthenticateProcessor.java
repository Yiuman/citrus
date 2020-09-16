package com.github.yiuman.citrus.security.authenticate;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 认证处理器
 *
 * @author yiuman
 * @date 2020/4/3
 */
public interface AuthenticateProcessor {


    /**
     * 找到对应的认证服务类
     *
     * @param mode 模式字符串
     * @return 匹配的认证服务类实现
     * @throws AuthenticationException 认证异常
     */
    AuthenticateService findByMode(String mode) throws AuthenticationException;

    /**
     * 根据请求转化成Security认证实体
     *
     * @param request 当前请求
     * @return Security认证实体
     * @throws AuthenticationException 认证异常
     */
    Authentication authenticate(HttpServletRequest request) throws AuthenticationException;

    /**
     * 生成token
     *
     * @param request 当前请求
     * @return token令牌
     * @see JwtToken
     */
    String token(HttpServletRequest request);

    /**
     * 根据当前请求解析为Security认证对象
     *
     * @param request 当前请求
     * @return Optional包装的身份认证信息
     */
    Optional<Authentication> resolve(HttpServletRequest request);

}