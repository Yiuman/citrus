package com.github.yiuman.citrus.system.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @author yiuman
 * @date 2020/4/6
 */
@Data
public class OrganDto {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long organId;

    /**
     * 组织名
     */
    private String organName;

    /**
     * 组织代码
     */
    private String organCode;

    /**
     * 上级ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;

    /**
     * 描述说明
     */
    private String describe;

}
