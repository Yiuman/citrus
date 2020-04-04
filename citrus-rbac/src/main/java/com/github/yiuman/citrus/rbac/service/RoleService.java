package com.github.yiuman.citrus.rbac.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yiuman.citrus.rbac.mapper.RoleMapper;
import com.github.yiuman.citrus.rbac.entity.Role;
import org.springframework.stereotype.Service;

/**
 * 角色权限相关逻辑类
 *
 * @author yiuman
 * @date 2020/3/31
 */
@Service
public class RoleService extends ServiceImpl<RoleMapper, Role> {

    public boolean hasPermission(Long userId, String resourceId) {
        return baseMapper.hasPermission(userId, resourceId);
    }
}
