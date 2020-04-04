package com.github.yiuman.citrus.rbac.entity;

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

    private Integer roleId;

    private Role role;

    private Integer authorityId;

    private Authority authority;

}
