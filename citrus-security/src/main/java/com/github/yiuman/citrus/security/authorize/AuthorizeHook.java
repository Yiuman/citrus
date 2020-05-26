package com.github.yiuman.citrus.security.authorize;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * 鉴权钩子，用于执行当前请求的鉴权
 *
 * @author yiuman
 * @date 2020/3/23
 */
public interface AuthorizeHook {

    /**
     * 用于判断当前用户是否有权限访问当前请求
     *
     * @param httpServletRequest 当前的请求
     * @param authentication     当前的身份认证信息
     * @return 若有权限则返回true, 反之false
     */
    boolean hasPermission(HttpServletRequest httpServletRequest, Authentication authentication);
}
