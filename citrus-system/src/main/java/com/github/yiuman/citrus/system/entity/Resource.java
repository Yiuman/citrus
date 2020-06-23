package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.yiuman.citrus.support.model.BaseTree;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 资源(可能是接口、菜单、操作等)
 *
 * @author yiuman
 * @date 2020/3/23
 */
@Data
@NoArgsConstructor
@TableName("sys_resource")
public class Resource extends BaseTree<Resource, Long> {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(type = IdType.ASSIGN_ID)
    private Long resourceId;

    /**
     * 资源名
     */
    private String resourceName;

    /**
     * 资源类型
     * 菜单:0
     * 操作:2
     */
    private Integer type;

    /**
     * 父资源ID
     */
    private Long parentId;

    /**
     * 资源路径
     */
    private String path;

    /**
     * 操作类型
     */
    private String operation;

    public Resource(String resourceName, Integer type, Long parentId, String path, String operation) {
        this.resourceName = resourceName;
        this.type = type;
        this.parentId = parentId;
        this.path = path;
        this.operation = operation;
    }

    @JsonSerialize(using = ToStringSerializer.class)
    @Override
    public Long getId() {
        return this.resourceId;
    }

    @Override
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Resource)) return false;

        Resource resource = (Resource) o;

        return resourceId != null ? resourceId.equals(resource.resourceId) : resource.resourceId == null;
    }

    @Override
    public int hashCode() {
        return resourceId != null ? resourceId.hashCode() : 0;
    }
}
