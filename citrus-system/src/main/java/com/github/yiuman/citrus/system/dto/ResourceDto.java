package com.github.yiuman.citrus.system.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 资源实体传输类
 *
 * @author yiuman
 * @date 2020/4/6
 */
@Data
@NoArgsConstructor
public class ResourceDto {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long resourceId;

    /**
     * 资源名
     */
    @NotBlank
    private String resourceName;

    /**
     * 资源类型
     */
    private Integer type = 0;

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

    private String resourceCode;

    public ResourceDto(@NotBlank String resourceName, Integer type, Long parentId, String path, String operation) {
        this.resourceName = resourceName;
        this.type = type;
        this.parentId = parentId;
        this.path = path;
        this.operation = operation;
    }

    public ResourceDto(@NotBlank String resourceName, Integer type, Long parentId, String path, String operation, String resourceCode) {
        this.resourceName = resourceName;
        this.type = type;
        this.parentId = parentId;
        this.path = path;
        this.operation = operation;
        this.resourceCode = resourceCode;
    }
}
