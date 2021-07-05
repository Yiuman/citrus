package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.yiuman.citrus.support.crud.AbstractAuditingEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色或角色组
 *
 * @author yiuman
 * @date 2020/3/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_role")
public class Role extends AbstractAuditingEntity {

    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 父角色Id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer parentId;

    /**
     * 0：角色 1：角色组（）
     */
    private Integer type;

    /**
     * 排序ID
     */
    private Integer orderId;

    private Boolean admin;

}
