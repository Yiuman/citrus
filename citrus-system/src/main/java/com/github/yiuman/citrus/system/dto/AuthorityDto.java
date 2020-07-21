package com.github.yiuman.citrus.system.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.yiuman.citrus.system.entity.AuthorityResource;
import lombok.Data;

import java.util.List;

/**
 * 权限dto
 *
 * @author yiuman
 * @date 2020/4/11
 */
@Data
public class AuthorityDto {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long authorityId;

    /**
     * 权限名称
     */
    private String authorityName;

    private String remark;

    /**
     * 当前权限包含的权限资源集合
     *
     * @see AuthorityResource
     */
    private List<AuthorityResource> resources;

}
