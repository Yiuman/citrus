package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
    private String id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

    @TableField(exist = false)
    private Role role;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long authorityId;

    @TableField(exist = false)
    private Authority authority;

    public RoleAuthority(Long roleId, Long authorityId) {
        this.roleId = roleId;
        this.authorityId = authorityId;
    }

    public String getId() {
        return String.format("%s-%s", role, authorityId);
    }
}
