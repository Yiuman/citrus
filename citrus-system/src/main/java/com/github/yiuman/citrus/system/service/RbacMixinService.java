package com.github.yiuman.citrus.system.service;

import com.github.yiuman.citrus.system.cache.UserOnlineCache;
import com.github.yiuman.citrus.system.dto.UserOnlineInfo;
import com.github.yiuman.citrus.system.entity.Authority;
import com.github.yiuman.citrus.system.entity.AuthorityResource;
import com.github.yiuman.citrus.system.entity.Resource;
import com.github.yiuman.citrus.system.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;

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

    private final UserOnlineCache userOnlineCache;

    /**
     * 判断当前用户是否有权限访问当前资源
     * @param user 当前用户
     * @param resource 当前资源
     * @return true/false
     */
    public boolean hasPermission(User user, Resource resource) {
        UserOnlineInfo userOnlineInfo = userOnlineCache.find(user.getUuid());
        if(userOnlineInfo.getAdmin()){
            return true;
        }
        //查出所有的权限
        Set<AuthorityResource> authorityResources = userOnlineInfo.getAuthorityResources();
        if (CollectionUtils.isEmpty(authorityResources)) {
            authorityResources =  authorityService.selectAuthorityResourceByUserIdAndResourceId(user.getUserId());
            userOnlineInfo.setAuthorityResources(authorityResources);
        }

        return authorityResources.stream().anyMatch(item -> item.getResourceId().equals(resource.getResourceId()));
    }
}
