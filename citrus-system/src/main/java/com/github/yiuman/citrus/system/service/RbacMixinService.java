package com.github.yiuman.citrus.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yiuman.citrus.support.crud.query.QueryWrapperHelper;
import com.github.yiuman.citrus.system.dto.UserOnlineInfo;
import com.github.yiuman.citrus.system.entity.AuthorityResource;
import com.github.yiuman.citrus.system.entity.Resource;
import com.github.yiuman.citrus.system.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    private final ScopeService scopeService;

    private final MenuService menuService;

    /**
     * 判断当前用户是否有权限访问当前资源
     *
     * @param user     当前用户
     * @param resource 当前资源
     * @return true/false
     */
    public boolean hasPermission(User user, Resource resource) {
        UserOnlineInfo userOnlineInfo = userService.getUserOnlineCache().find(user.getUuid());
        if (userOnlineInfo.getAdmin()) {
            return true;
        }
        //查出所有的权限
        Set<AuthorityResource> authorityResources = userOnlineInfo.getAuthorityResources();
        if (CollectionUtils.isEmpty(authorityResources)) {
            authorityResources = authorityService.getAuthorityResourceByUserIdAndResourceId(user.getUserId());
            userOnlineInfo.setAuthorityResources(authorityResources);
        }

        return authorityResources.stream().anyMatch(item -> item.getResourceId().equals(resource.getResourceId()));
    }

    /**
     * 设置用户拥有的信息（角色、部门、资源等）
     *
     * @param userOnlineInfo 在线用户
     */
    public void setUserOwnedInfo(UserOnlineInfo userOnlineInfo) {
        if (userOnlineInfo.getAdmin()) {
            userOnlineInfo.setMenus(menuService.list());
        } else {
            final Long userId = userOnlineInfo.getUserId();
            userOnlineInfo.setRoles(userService.getRolesByUserId(userId));
            userOnlineInfo.setOrganizations(userService.getUserOrgansByUserId(userId));
            Set<Resource> userResources = authorityService.getUserResources(userId);
            
            userOnlineInfo.setMenus(userResources.stream().filter(resource -> 0 == resource.getType()).collect(Collectors.toList()));
            userOnlineInfo.getMenus().sort(new Comparator<Resource>() {
				@Override
				public int compare(Resource o1, Resource o2) {
					return o1.getId()<o2.getId()?-1:1;
				}
			});
            //加入上一层菜单
            QueryWrapper<Resource> qw = new QueryWrapper<>();
            qw.in("resource_id", userOnlineInfo.getMenus().stream().filter(r -> r.getParentId()!=null).map(Resource::getParentId).distinct().collect(Collectors.toList()));
            userOnlineInfo.getMenus().addAll(menuService.list(qw));            
            userOnlineInfo.setResources(userResources);
            userOnlineInfo.setAuthorities(authorityService.getAuthoritiesByUserId(userId));
            userOnlineInfo.setAuthorityResources(authorityService.getAuthorityResourceByUserIdAndResourceId(userId));
        }

    }
}
