package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.yiuman.citrus.system.commons.model.AbstractAuditingEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.ClobTypeHandler;

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

    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 登录ID
     */
    private String loginId;

    @JsonIgnore
    private String password;

    private String username;

    private String mobile;

    private String uuid;

    private String email;

    /**
     * 是否管理员
     */
    private Boolean admin;

    @TableField(typeHandler = ClobTypeHandler.class)
    private String avatar;

    /**
     * 用户状态
     */
    private Integer status;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;

}
