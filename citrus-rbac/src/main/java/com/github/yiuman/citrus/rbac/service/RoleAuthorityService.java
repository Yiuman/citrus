package com.github.yiuman.citrus.rbac.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yiuman.citrus.rbac.mapper.RoleAuthorityMapper;
import com.github.yiuman.citrus.rbac.entity.RoleAuthority;
import org.springframework.stereotype.Service;

/**
 * @author yiuman
 * @date 2020/3/31
 */
@Service
public class RoleAuthorityService extends ServiceImpl<RoleAuthorityMapper, RoleAuthority> {

    public RoleAuthorityService() {
    }

}
