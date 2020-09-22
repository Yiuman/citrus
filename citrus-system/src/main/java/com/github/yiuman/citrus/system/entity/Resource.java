package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.yiuman.citrus.support.model.BaseTree;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(of = {"resourceId"}, callSuper = false)
public class Resource extends BaseTree<Resource, Long> {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(type = IdType.ASSIGN_ID)
    private Long resourceId;

    /**
     * 资源名
     */
    private String resourceName;

    /**
     * 是否隐藏
     */
    private Boolean hidden ;

    /**
     * 导入的组件路径
     */
    private String component;

    /**
     * 菜单的图标
     */
    private String icon;

    /**
     * 资源类型
     * 菜单:0
     * 操作:2
     */
    private Integer type;

    /**
     * 父资源ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;

    /**
     * 资源路径
     */
    private String path;

    /**
     * 操作类型
     */
    private String operation;

    /**
     * 资源代码
     */
    private String resourceCode;

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

}
