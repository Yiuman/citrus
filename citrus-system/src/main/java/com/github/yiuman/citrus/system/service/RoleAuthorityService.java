package com.github.yiuman.citrus.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yiuman.citrus.system.entity.RoleAuthority;
import com.github.yiuman.citrus.system.mapper.RoleAuthorityMapper;
import org.springframework.stereotype.Service;

/**
 * 角色权限逻辑层
 *
 * @author yiuman
 * @date 2020/3/31
 */
@Service
public class RoleAuthorityService extends ServiceImpl<RoleAuthorityMapper, RoleAuthority> {

    public RoleAuthorityService() {
    }

}
