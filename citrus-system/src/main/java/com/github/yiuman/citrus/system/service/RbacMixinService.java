package com.github.yiuman.citrus.system.service;

import cn.hutool.core.collection.CollectionUtil;
import com.github.yiuman.citrus.support.crud.query.Query;
import com.github.yiuman.citrus.system.dto.UserOnlineInfo;
import com.github.yiuman.citrus.system.entity.AuthorityResource;
import com.github.yiuman.citrus.system.entity.Resource;
import com.github.yiuman.citrus.system.entity.User;
import com.github.yiuman.citrus.system.enums.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
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
            Set<Resource> userResourcesWithAll = getUserResourcesWithAll(userId);
            //菜单排序 todo 考虑添加一个orderId？
            Comparator<? super Resource> menuSortComparator = (r1, r2) -> (int) (r1.getId() - r2.getId());
            List<Resource> menus = userResourcesWithAll.stream()
                    .filter(resource -> ResourceType.MENU == resource.getType()).sorted(menuSortComparator)
                    .collect(Collectors.toList());

            userOnlineInfo.setMenus(menus);
            userOnlineInfo.setResources(userResourcesWithAll);
            userOnlineInfo.setAuthorities(authorityService.getAuthoritiesByUserId(userId));
            userOnlineInfo.setAuthorityResources(authorityService.getAuthorityResourceByUserIdAndResourceId(userId));
        }

    }

    /**
     * 获取用户的所有资源
     *
     * @param userId 当前用户ID
     * @return 资源集合
     */
    public Set<Resource> getUserResourcesWithAll(Long userId) {
        Set<Resource> userResources = authorityService.getUserResources(userId);
        userResources.addAll(getParentMenus(userResources));
        return userResources;
    }

    /**
     * 找到上级的菜单
     *
     * @param userResources 用户菜单资源
     * @return 获取父级菜单集合
     */
    public Set<Resource> getParentMenus(Collection<Resource> userResources) {
        List<Resource> menus = userResources.stream()
                .filter(resource -> ResourceType.MENU == resource.getType())
                .collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(menus)) {
            Set<Long> parentIds = menus.stream()
                    .filter(resource -> Objects.nonNull(resource.getParentId()))
                    .map(Resource::getParentId).collect(Collectors.toSet());
            if (CollectionUtil.isEmpty(parentIds)) {
                return Collections.emptySet();
            }

            List<Resource> parentResources = menuService.list(Query.of().in(resourceService.getKeyColumn(), parentIds));
            if (CollectionUtil.isNotEmpty(parentResources)) {
                parentResources.addAll(getParentMenus(parentResources));
            }

            return new HashSet<>(parentResources);
        }
        return Collections.emptySet();
    }

}
