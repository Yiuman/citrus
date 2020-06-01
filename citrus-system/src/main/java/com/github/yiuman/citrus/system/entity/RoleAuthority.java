package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色权限映射
 *
 * @author yiuman
 * @date 2020/3/30
 */
@Data
@TableName("sys_role_auth")
public class RoleAuthority {

    @TableId
    private Long roleId;

    private Role role;

    @TableId
    private Long authorityId;

    private Authority authority;

    public RoleAuthority(Long roleId, Long authorityId) {
        this.roleId = roleId;
        this.authorityId = authorityId;
    }
}
