package com.github.yiuman.citrus.rbac.service;

import com.github.yiuman.citrus.rbac.entity.User;
import com.github.yiuman.citrus.security.authorize.AuthorizeHook;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 判断是否登录的权限钩子
 *
 * @author yiuman
 * @date 2020/4/4
 */
@Component
public class HasLoginHook implements AuthorizeHook {

    private final UserService userService;

    public HasLoginHook(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean hasPermission(HttpServletRequest httpServletRequest, Authentication authentication) {
        if (authentication == null) {
            return false;
        }

        Object principal = authentication.getPrincipal();

        //匿名不给进
        if ("anonymousUser".equals(principal)) {
            return false;
        }

        User user = null;
        if (principal instanceof User) {
            user = (User) principal;
        } else if (principal instanceof String) {
            user = userService.getUserByUUID((String) principal);
        }

        return user != null;

    }
}
