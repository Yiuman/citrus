package com.github.yiuman.citrus.rbac.service;

import com.github.yiuman.citrus.rbac.entity.Resource;
import com.github.yiuman.citrus.rbac.entity.User;
import com.github.yiuman.citrus.security.authorize.AuthorizeHook;
import com.github.yiuman.citrus.security.authorize.AuthorizeServiceHook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 鉴权服务类
 *
 * @author yiuman
 * @date 2020/3/23
 */
@Service
@Slf4j
public class RbacService implements AuthorizeServiceHook {

    private final UserService userService;

    private final RoleService roleService;

    private final ResourceService resourceService;

    public RbacService(UserService userService, RoleService roleService, ResourceService resourceService) {
        this.userService = userService;
        this.roleService = roleService;
        this.resourceService = resourceService;
    }

    @Override
    public boolean hasPermission(HttpServletRequest httpServletRequest, Authentication authentication) {
        try {
            //没有配置资源的情况下都可以访问
            Resource resource = resourceService.selectByUri(httpServletRequest.getRequestURI(), httpServletRequest.getMethod());
            if (resource == null) {
                return true;
            }

            Object principal = authentication.getPrincipal();
            User user = null;
            if (principal instanceof User) {
                user = (User) principal;
            } else if (principal instanceof String) {
                user = userService.getUserByUUID((String) principal);
            }

            if (user == null) {
                return false;
            }


            return roleService.hasPermission(user.getUserId(), resource.getResourceId());
        } catch (Exception e) {
            log.error("RbacService Exception", e);
            return false;
        }


    }
}
