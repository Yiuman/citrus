package com.github.yiuman.citrus.system.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.yiuman.citrus.system.entity.Organization;
import com.github.yiuman.citrus.system.entity.ScopeDefine;
import lombok.Data;

import java.util.List;

/**
 * @author yiuman
 * @date 2020/6/1
 */
@Data
public class ScopeDto {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long scopeId;

    private String scopeName;

    /**
     * 所属组织,通用的为-1
     *
     * @see Organization
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long organId;

    private List<ScopeDefine> scopeDefines;

}
