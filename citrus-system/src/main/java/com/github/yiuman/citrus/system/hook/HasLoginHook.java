package com.github.yiuman.citrus.system.hook;

import com.github.yiuman.citrus.security.authorize.AuthorizeHook;
import com.github.yiuman.citrus.system.service.UserService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class HasLoginHook implements AuthorizeHook {

    private final UserService userService;

    @Override
    public boolean hasPermission(HttpServletRequest httpServletRequest, Authentication authentication) {
        if (authentication == null) {
            return false;
        }

        return userService.getCurrentUser().isPresent();
    }
}
