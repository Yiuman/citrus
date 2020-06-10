package com.github.yiuman.citrus.system.dto;

import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * 权限dto
 *
 * @author yiuman
 * @date 2020/4/11
 */
@Data
public class AuthorityDto {

    private Long authorityId;

    /**
     * 权限名称
     */
    private String authorityName;

    private Integer scopeId;

    private String describe;

    /**
     * 资源
     * 主键是资源的ID，例如菜单ID
     *
     * @see com.github.yiuman.citrus.system.entity.Resource
     * 值是资源的数据范围及可操作资源的集合
     */
    private Map<Long, ResourceScopeAndOperations> resources;

    @Data
    static class ResourceScopeAndOperations {

        private Long scopeId;

        private Set<Long> operations;

    }
}
