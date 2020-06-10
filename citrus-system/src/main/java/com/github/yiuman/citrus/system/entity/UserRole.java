package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;

import java.util.Arrays;

/**
 * 用户角色映射
 * 用于记录某个用户在某个组织中拥有哪些角色
 *
 * @author yiuman
 * @date 2020/3/30
 */
@Data
@TableName("sys_user_role")
public class UserRole {

    private Long userId;

    private Long roleId;

    private Long organId;

    public UserRole() {
    }

    public UserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
