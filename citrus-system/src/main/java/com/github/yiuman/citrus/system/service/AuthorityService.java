package com.github.yiuman.citrus.system.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yiuman.citrus.support.crud.service.BaseDtoService;
import com.github.yiuman.citrus.system.dto.AuthorityDto;
import com.github.yiuman.citrus.system.entity.Authority;
import com.github.yiuman.citrus.system.entity.AuthorityResource;
import com.github.yiuman.citrus.system.mapper.AuthorityMapper;
import com.github.yiuman.citrus.system.mapper.AuthorityResourceMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * 权限操作逻辑层
 *
 * @author yiuman
 * @date 2020/4/11
 */
@Service
public class AuthorityService extends BaseDtoService<Authority, Long, AuthorityDto> {

    private final AuthorityMapper authorityMapper;

    private final AuthorityResourceMapper authorityResourceMapper;

    public AuthorityService(AuthorityMapper authorityMapper, AuthorityResourceMapper authorityResourceMapper) {
        this.authorityMapper = authorityMapper;
        this.authorityResourceMapper = authorityResourceMapper;
    }

    @Override
    public void afterSave(AuthorityDto entity) {
        //保存资源与权限的关系
        Set<Long> resourceIds = entity.getResourceIds();
        if (!CollectionUtils.isEmpty(resourceIds)) {
            resourceIds.forEach(resourceId -> authorityResourceMapper.saveEntity(new AuthorityResource(entity.getAuthorityId(), resourceId)));
        }

    }

    @Override
    protected BaseMapper<Authority> getBaseMapper() {
        return authorityMapper;
    }
}
