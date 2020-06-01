package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class Role extends AbstractAuditingEntity{

    @TableId(type = IdType.AUTO)
    private Long roleId;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 父角色Id
     */
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
