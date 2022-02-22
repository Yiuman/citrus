package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 用户角色映射
 * 用于记录某个用户在某个组织中拥有哪些角色
 *
 * @author yiuman
 * @date 2020/3/30
 */
@Data
@TableName("sys_user_role")
@NoArgsConstructor
public class UserRole {

    @TableId
    @TableField(exist = false)
    private String id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long organId;

    @TableField(exist = false)
    private Role role;

    public String getId() {
        return String.format("%s-%s-%s", userId, roleId, organId);
    }

    public String getRoleName() {
        return Objects.nonNull(role) ? role.getRoleName() : null;
    }
}
