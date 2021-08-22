package com.github.yiuman.citrus.system.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.support.crud.service.BaseDtoService;
import com.github.yiuman.citrus.system.dto.AuthorityDto;
import com.github.yiuman.citrus.system.entity.Authority;
import com.github.yiuman.citrus.system.entity.AuthorityResource;
import com.github.yiuman.citrus.system.entity.Resource;
import com.github.yiuman.citrus.system.entity.RoleAuthority;
import com.github.yiuman.citrus.system.mapper.AuthorityResourceMapper;
import com.github.yiuman.citrus.system.mapper.RoleAuthorityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 权限操作逻辑层
 *
 * @author yiuman
 * @date 2020/4/11
 */
@Service
@AllArgsConstructor
public class AuthorityService extends BaseDtoService<Authority, Long, AuthorityDto> {

    private final AuthorityResourceMapper authorityResourceMapper;
    private final RoleAuthorityMapper roleAuthorityMapper;

    @Override
    public AuthorityDto get(Long key) {
        AuthorityDto authorityDto = super.get(key);
        //查询当前权限的资源列表，菜单资源及菜单操作
        List<AuthorityResource> authorityResources = authorityResourceMapper.selectList(
                Wrappers.<AuthorityResource>query()
                        .eq(getKeyColumn(), authorityDto.getAuthorityId())
                        .lambda().eq(AuthorityResource::getType, 0)
        );
        authorityResources.forEach(item -> item.setOperations(
                authorityResourceMapper.selectList(
                        Wrappers.<AuthorityResource>query()
                                .eq(getKeyColumn(), authorityDto.getAuthorityId())
                                .lambda()
                                .eq(AuthorityResource::getObjectId, item.getResourceId())
                                .eq(AuthorityResource::getType, 2))
                )
        );

        authorityDto.setResources(authorityResources);
        return authorityDto;
    }

    @Override
    public void afterSave(AuthorityDto entity) {
        //保存资源与权限的关系
        List<AuthorityResource> resources = entity.getResources();

        //先删掉旧的
        if (entity.getAuthorityId() != null) {
            authorityResourceMapper.delete(Wrappers.<AuthorityResource>query()
                    .eq(getKeyColumn(), entity.getAuthorityId()));
        }

        if (!CollectionUtils.isEmpty(resources)) {
            final List<AuthorityResource> allResource = new ArrayList<>(resources);
            //把操作资源也加到权限资源里边去
            resources.forEach(item -> allResource.addAll(item.getOperations()));
            allResource.forEach(item -> item.setAuthorityId(entity.getAuthorityId()));
            authorityResourceMapper.saveBatch(allResource);
        }

    }

    /**
     * 根据用户I查询此用户的权限与资源配置的集合
     *
     * @param userId 用户ID
     * @return 权限资源结合
     */
    public Set<AuthorityResource> getAuthorityResourceByUserIdAndResourceId(Long userId) {
        return authorityResourceMapper.getAuthorityResourceByUserIdAndResourceId(userId);
    }

    public Set<Resource> getUserResources(Long userId) {
        return authorityResourceMapper.getResourcesByUserId(userId);
    }

    public Set<Authority> getAuthoritiesByUserId(Long userId) {
        return authorityResourceMapper.getAuthoritiesByUserId(userId);
    }

    @Override
    public boolean beforeRemove(AuthorityDto entity) {

        authorityResourceMapper.delete(Wrappers.<AuthorityResource>lambdaQuery().eq(AuthorityResource::getAuthorityId, entity.getAuthorityId()));
        roleAuthorityMapper.delete(Wrappers.<RoleAuthority>lambdaQuery().eq(RoleAuthority::getAuthorityId, entity.getAuthorityId()));
        return super.beforeRemove(entity);
    }


}
