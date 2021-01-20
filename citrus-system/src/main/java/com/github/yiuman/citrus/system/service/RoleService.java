package com.github.yiuman.citrus.system.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.support.crud.service.BaseDtoService;
import com.github.yiuman.citrus.system.dto.RoleDto;
import com.github.yiuman.citrus.system.entity.Authority;
import com.github.yiuman.citrus.system.entity.Role;
import com.github.yiuman.citrus.system.entity.RoleAuthority;
import com.github.yiuman.citrus.system.mapper.RoleAuthorityMapper;
import com.github.yiuman.citrus.system.mapper.RoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色权限相关逻辑类
 *
 * @author yiuman
 * @date 2020/3/31
 */
@Service
public class RoleService extends BaseDtoService<Role, Long, RoleDto> {

    private final RoleMapper roleMapper;

    private final RoleAuthorityMapper roleAuthorityMapper;

    public RoleService(RoleMapper roleMapper, RoleAuthorityMapper roleAuthorityMapper) {
        this.roleMapper = roleMapper;
        this.roleAuthorityMapper = roleAuthorityMapper;
    }

    @Override
    public void afterSave(RoleDto entity) throws Exception {
        List<Long> authIds = entity.getAuthIds();
        if (!CollectionUtils.isEmpty(authIds)) {
            //删除旧的关联关系
            roleAuthorityMapper.delete(
                    Wrappers.<RoleAuthority>query()
                            .eq(getKeyColumn(), entity.getRoleId())
            );

            roleAuthorityMapper.saveBatch(authIds.stream()
                    .map(authId -> new RoleAuthority(entity.getRoleId(), authId)).collect(Collectors.toList()));
        }

    }

    @Override
    protected BaseMapper<Role> getBaseMapper() {
        return roleMapper;
    }

    public List<Authority> getAuthoritiesByRoleId(Long roleId) {
        return roleMapper.selectAuthoritiesByRoleId(roleId);
    }

    /**
     * 根据用户获取权限集合
     *
     * @param userId 用户ID
     * @return 权限集合
     */
    public Set<Authority> getAuthoritiesByUserId(Long userId) {
        return roleMapper.selectAuthoritiesByUserId(userId);
    }

    public boolean hasPermission(Long userId, Long resourceId) throws Exception {
        if (roleAuthorityMapper.selectCount(Wrappers.<RoleAuthority>query().eq(getKeyColumn(), resourceId)) == 0) {
            return true;
        }

        return roleMapper.hasPermission(userId, resourceId);
    }

    public List<RoleAuthority> getRoleAuthorityByAuthAuthIds(List<Long> authIds) {
        return roleAuthorityMapper.selectList(Wrappers.<RoleAuthority>lambdaQuery().in(RoleAuthority::getAuthorityId, authIds));
    }


}
