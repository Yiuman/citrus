package com.github.yiuman.citrus.rbac.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 资源(可能是接口、菜单、操作等)
 *
 * @author yiuman
 * @date 2020/3/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_resource")
public class Resource extends AbstractAuditingEntity{

    @TableId(type = IdType.ASSIGN_UUID)
    private String resourceId;

    /**
     * 资源名
     */
    private String resourceName;

    /**
     * 资源类型
     */
    private Integer type;

    /**
     * 父资源ID
     */
    private String parentId;

    /**
     * 资源路径
     */
    private String path;

    /**
     * 操作类型
     */
    private String operation;

}
