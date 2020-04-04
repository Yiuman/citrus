package com.github.yiuman.citrus.security.authorize;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yiuman
 * @date 2020/3/23
 */
public interface AuthorizeHook {

    boolean hasPermission(HttpServletRequest httpServletRequest, Authentication authentication);
}
