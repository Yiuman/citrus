package com.github.yiuman.citrus.system.service;

import com.github.yiuman.citrus.system.entity.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色资源权限控制混合服务类
 * 用于处理用户角色权限的相关逻辑
 *
 * @author yiuman
 * @date 2020/6/17
 */
@Service
@AllArgsConstructor
@Getter
public class RbacMixinService {

    private final UserService userService;

    private final RoleService roleService;

    private final ResourceService resourceService;

    private final AuthorityService authorityService;

    private final OrganService organService;

    public boolean hasPermission(Long userId, Long resourceId) {
        List<Authority> authoritiesByUserId = roleService.getAuthoritiesByUserId(userId);
        return true;
    }
}
