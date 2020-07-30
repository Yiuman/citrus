package com.github.yiuman.citrus.system.dto;

import com.github.yiuman.citrus.system.entity.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Set;

/**
 * 咋西安用户信息
 *
 * @author yiuman
 * @date 2020/6/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserOnlineInfo extends User {

    /**
     * 用户角色
     */
    private List<Role> roles;

    /**
     * 用户部门
     */
    private List<Organization> organizations;

    /**
     * 用户拥有的菜单
     */
    private List<Resource> menus;

    /**
     * 用户权限
     */
    private Set<Authority> authorities;

    /**
     * 用户资源
     */
    private Set<Resource> resources;

    /**
     * 权限与资源
     */
    private Set<AuthorityResource> authorityResources;

    public static UserOnlineInfo newInstance(User user) {
        UserOnlineInfo userOnlineInfo = new UserOnlineInfo();
        BeanUtils.copyProperties(user, userOnlineInfo);
        return userOnlineInfo;
    }

}
