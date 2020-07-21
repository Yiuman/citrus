package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.yiuman.citrus.system.commons.model.AbstractAuditingEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统用户表
 *
 * @author yiuman
 * @date 2020/3/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_user")
public class User extends AbstractAuditingEntity {

    @TableId(type = IdType.AUTO)
    private Long userId;

    /**
     * 登录ID
     */
    private String loginId;

    private String password;

    private String username;

    private String mobile;

    private String uuid;

    private String email;

    /**
     * 是否管理员
     */
    private Boolean admin;

    /**
     * 用户状态
     */
    private Integer status;

    /**
     * 乐观锁
     */
    private Integer version;

}
