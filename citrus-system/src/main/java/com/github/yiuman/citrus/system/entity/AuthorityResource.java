package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 权限与资源的映射
 *
 * @author yiuman
 * @date 2020/3/30
 */
@Data
@TableName("sys_auth_resource")
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityResource {

    @TableId
    private String id;

    /**
     * 关联的权限实体ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long authorityId;

    /**
     * 关联的数据范围ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long scopeId;

    /**
     * 关联的对象，如果资源类型为"操作"，即关联的对象为该"操作对应的资源ID"
     * 例如 菜单与新增、删除等操作，此实体中的resourceId为操作类型的ID，即此objectId为此操作对应的菜单
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long objectId;

    /**
     * 资源ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long resourceId;

    /**
     * 对应的资源类型（冗余）
     *
     * @see Resource
     */
    private Integer type;

    /**
     * 关联的操作
     */
    @TableField(exist = false)
    private List<AuthorityResource> operations;

    public String getId() {
        return String.format("%s-%s", authorityId, resourceId);
    }
}
