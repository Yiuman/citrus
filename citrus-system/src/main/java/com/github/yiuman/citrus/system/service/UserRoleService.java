package com.github.yiuman.citrus.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yiuman.citrus.system.entity.UserRole;
import com.github.yiuman.citrus.system.mapper.UserRoleMapper;
import org.springframework.stereotype.Component;

/**
 * 用户角色逻辑层
 *
 * @author yiuman
 * @date 2020/4/7
 */
@Component
public class UserRoleService extends ServiceImpl<UserRoleMapper, UserRole> {

    public void removeByUserIdAndOrganId(Long userId, Long organId) {
        this.remove(new QueryWrapper<UserRole>()
                .eq("user_Id", userId)
                .eq("orginId", organId));
    }
}
