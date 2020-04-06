package com.github.yiuman.citrus.rbac.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yiuman.citrus.rbac.dto.RoleDto;
import com.github.yiuman.citrus.rbac.mapper.RoleMapper;
import com.github.yiuman.citrus.rbac.entity.Role;
import com.github.yiuman.citrus.support.crud.BaseCrudController;
import com.github.yiuman.citrus.support.crud.BaseDtoCrudService;
import org.springframework.stereotype.Service;

/**
 * 角色权限相关逻辑类
 *
 * @author yiuman
 * @date 2020/3/31
 */
@Service
public class RoleService extends BaseDtoCrudService<RoleMapper,Role, RoleDto,Long>  {

    public boolean hasPermission(Long userId, String resourceId) {
        return baseMapper.hasPermission(userId, resourceId);
    }
}
