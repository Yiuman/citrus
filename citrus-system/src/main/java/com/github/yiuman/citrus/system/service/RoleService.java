package com.github.yiuman.citrus.system.service;

import com.github.yiuman.citrus.support.crud.service.BaseDtoCrudService;
import com.github.yiuman.citrus.system.dto.RoleDto;
import com.github.yiuman.citrus.system.entity.Role;
import com.github.yiuman.citrus.system.entity.RoleAuthority;
import com.github.yiuman.citrus.system.mapper.RoleAuthorityMapper;
import com.github.yiuman.citrus.system.mapper.RoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 角色权限相关逻辑类
 *
 * @author yiuman
 * @date 2020/3/31
 */
@Service
public class RoleService extends BaseDtoCrudService<RoleMapper, Role, RoleDto, Long> {

    private final RoleAuthorityMapper roleAuthorityMapper;

    public RoleService(RoleAuthorityMapper roleAuthorityMapper) {
        this.roleAuthorityMapper = roleAuthorityMapper;
    }

    @Override
    public void afterSave(RoleDto entity) throws Exception {
        List<Long> authIds = entity.getAuthIds();
        if(!CollectionUtils.isEmpty(authIds)){
            authIds.forEach(authId->roleAuthorityMapper.saveEntity(new RoleAuthority(entity.getRoleId(),authId)));
        }

    }

    public boolean hasPermission(Long userId, Long resourceId) {
        return getBaseMapper().hasPermission(userId, resourceId);
    }

}
