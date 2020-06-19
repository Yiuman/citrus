package com.github.yiuman.citrus.system.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.support.crud.service.BaseDtoService;
import com.github.yiuman.citrus.system.dto.AuthorityDto;
import com.github.yiuman.citrus.system.entity.Authority;
import com.github.yiuman.citrus.system.entity.AuthorityResource;
import com.github.yiuman.citrus.system.mapper.AuthorityMapper;
import com.github.yiuman.citrus.system.mapper.AuthorityResourceMapper;
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

    private final AuthorityMapper authorityMapper;

    private final AuthorityResourceMapper authorityResourceMapper;

    @Override
    public AuthorityDto get(Long key) {
        AuthorityDto authorityDto = super.get(key);
        //查询当前权限的资源列表，菜单资源及菜单操作
        List<AuthorityResource> authorityResources = authorityResourceMapper.selectList(
                Wrappers.<AuthorityResource>query()
                        .eq(getKeyColumn(), authorityDto.getAuthorityId())
                        .eq("type", 0)
        );
        authorityResources.forEach(item -> item.setOperations(
                authorityResourceMapper.selectList(
                        Wrappers.<AuthorityResource>query()
                                .eq(getKeyColumn(), authorityDto.getAuthorityId())
                                .eq("object_id", item.getResourceId())
                                .eq("type", 2))
                )
        );

        authorityDto.setResources(authorityResources);
        return authorityDto;
    }

    @Override
    public void afterSave(AuthorityDto entity) throws Exception {
        //保存资源与权限的关系
        List<AuthorityResource> resources = entity.getResources();

        //先删掉旧的
        if (entity.getAuthorityId() != null) {
            authorityResourceMapper.delete(Wrappers.<AuthorityResource>query()
                    .eq(getKeyColumn(), entity.getAuthorityId()));
        }

        if (!CollectionUtils.isEmpty(resources)) {
            final List<AuthorityResource> allResource = new ArrayList<>(resources);
            resources.forEach(item -> allResource.addAll(item.getOperations()));
            allResource.forEach(item -> item.setAuthorityId(entity.getAuthorityId()));
            authorityResourceMapper.saveBatch(allResource);
        }

    }

    @Override
    protected BaseMapper<Authority> getBaseMapper() {
        return authorityMapper;
    }

    /**
     * 根据用户I查询此用户的权限与资源配置的集合
     * @param userId 用户ID
     * @return 权限资源结合
     */
    public Set<AuthorityResource> selectAuthorityResourceByUserIdAndResourceId(Long userId) {
        return authorityResourceMapper.selectAuthorityResourceByUserIdAndResourceId(userId);
    }
}
