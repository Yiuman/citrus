package com.github.yiuman.citrus.system.dto;

import com.github.yiuman.citrus.system.entity.Organization;
import lombok.Data;

/**
 * @author yiuman
 * @date 2020/6/1
 */
@Data
public class ScopeDto {

    private Long scopeId;

    private String scopeName;

    /**
     * 所属组织,通用的为-1
     *
     * @see Organization
     */
    private Long organId;

}
