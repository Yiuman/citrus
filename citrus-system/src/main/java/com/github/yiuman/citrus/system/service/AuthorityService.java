package com.github.yiuman.citrus.system.service;

import com.github.yiuman.citrus.support.crud.service.BaseDtoCrudService;
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
public class AuthorityService extends BaseDtoCrudService<AuthorityMapper, Authority, AuthorityDto, Long> {

    private final AuthorityResourceMapper authorityResourceMapper;

    public AuthorityService(AuthorityResourceMapper authorityResourceMapper) {
        this.authorityResourceMapper = authorityResourceMapper;
    }

    @Override
    public void afterSave(AuthorityDto entity) throws Exception {
        //保存资源与权限的关系
        Set<Long> resourceIds = entity.getResourceIds();
        if (!CollectionUtils.isEmpty(resourceIds)) {
            resourceIds.forEach(resourceId -> authorityResourceMapper.saveEntity(new AuthorityResource(entity.getAuthorityId(), resourceId)));
        }

    }
}
