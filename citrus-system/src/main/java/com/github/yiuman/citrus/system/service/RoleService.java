package com.github.yiuman.citrus.system.service;

import com.github.yiuman.citrus.system.dto.RoleDto;
import com.github.yiuman.citrus.system.entity.Role;
import com.github.yiuman.citrus.system.mapper.RoleMapper;
import com.github.yiuman.citrus.support.crud.BaseDtoCrudService;
import org.springframework.stereotype.Service;

/**
 * 角色权限相关逻辑类
 *
 * @author yiuman
 * @date 2020/3/31
 */
@Service
public class RoleService extends BaseDtoCrudService<RoleMapper, Role, RoleDto, Long> {

    private final RoleAuthorityService roleAuthorityService;

    public RoleService(RoleAuthorityService roleAuthorityService) {
        this.roleAuthorityService = roleAuthorityService;
    }

    @Override
    public void afterSave(RoleDto entity) throws Exception {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    public boolean hasPermission(Long userId, String resourceId) {
        return baseMapper.hasPermission(userId, resourceId);
    }

}
