package com.github.yiuman.citrus.system.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.support.crud.CrudHelper;
import com.github.yiuman.citrus.support.crud.mapper.CrudMapper;
import com.github.yiuman.citrus.support.crud.service.BaseDtoService;
import com.github.yiuman.citrus.system.dto.RoleDto;
import com.github.yiuman.citrus.system.entity.Authority;
import com.github.yiuman.citrus.system.entity.Role;
import com.github.yiuman.citrus.system.entity.RoleAuthority;
import com.github.yiuman.citrus.system.entity.UserRole;
import com.github.yiuman.citrus.system.mapper.RoleAuthorityMapper;
import com.github.yiuman.citrus.system.mapper.RoleMapper;
import com.github.yiuman.citrus.system.mapper.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色权限相关逻辑类
 *
 * @author yiuman
 * @date 2020/3/31
 */
@Service
@RequiredArgsConstructor
public class RoleService extends BaseDtoService<Role, Long, RoleDto> {

    private final RoleMapper roleMapper;
    private final RoleAuthorityMapper roleAuthorityMapper;
    private final UserRoleMapper userRoleMapper;

    @Override
    public void afterSave(RoleDto entity) {
        List<Long> authIds = entity.getAuthIds();
        if (!CollectionUtils.isEmpty(authIds)) {
            //删除旧的关联关系
            roleAuthorityMapper.delete(Wrappers.<RoleAuthority>query().eq(getKeyColumn(), entity.getRoleId()));
            roleAuthorityMapper.saveBatch(authIds.stream()
                    .map(authId -> new RoleAuthority(entity.getRoleId(), authId))
                    .collect(Collectors.toList()));
        }

    }

    @Override
    protected CrudMapper<Role> getBaseMapper() {
        return roleMapper;
    }

    public List<Authority> getAuthoritiesByRoleId(Long roleId) {
        return roleMapper.selectAuthoritiesByRoleId(roleId);
    }

    public List<RoleAuthority> getRoleAuthorityByAuthIds(List<Long> authIds) {
        return roleAuthorityMapper.selectList(Wrappers.<RoleAuthority>lambdaQuery().in(RoleAuthority::getAuthorityId, authIds));
    }

    public List<RoleAuthority> getRoleAuthorityByRoleIds(Collection<Long> roleIds) {
        List<RoleAuthority> roleAuthorities = roleAuthorityMapper.selectList(Wrappers.<RoleAuthority>lambdaQuery().in(RoleAuthority::getRoleId, roleIds));
        if (CollUtil.isEmpty(roleAuthorities)) {
            return CollUtil.newArrayList();
        }

        Set<Long> authIds = roleAuthorities.stream().map(RoleAuthority::getAuthorityId).collect(Collectors.toSet());
        CrudMapper<Authority> crudMapper = CrudHelper.getCrudMapper(Authority.class);
        Map<Long, Authority> authorityMap = crudMapper.selectBatchIds(authIds).stream().collect(Collectors.toMap(Authority::getAuthorityId, authority -> authority));
        Map<Long, Role> roleMap = roleMapper.selectBatchIds(roleIds)
                .stream().collect(Collectors.toMap(Role::getRoleId, role -> role));
        roleAuthorities.forEach(roleAuthority -> {
            roleAuthority.setAuthority(authorityMap.get(roleAuthority.getAuthorityId()));
            roleAuthority.setRole(roleMap.get(roleAuthority.getRoleId()));
        });

        return roleAuthorities;
    }

    @Override
    public boolean beforeRemove(RoleDto entity) {

        userRoleMapper.delete(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getRoleId, entity.getRoleId()));
        roleAuthorityMapper.delete(Wrappers.<RoleAuthority>lambdaQuery().eq(RoleAuthority::getRoleId, entity.getRoleId()));
        return super.beforeRemove(entity);
    }


}
