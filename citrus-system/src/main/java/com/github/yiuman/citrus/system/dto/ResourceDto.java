package com.github.yiuman.citrus.system.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 资源实体传输类
 *
 * @author yiuman
 * @date 2020/4/6
 */
@Data
public class ResourceDto {

    private String resourceId;

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
