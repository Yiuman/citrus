package com.github.yiuman.citrus.system.dto;

import lombok.Data;

/**
 * @author yiuman
 * @date 2020/4/6
 */
@Data
public class OrganDto {

    private Integer organId;

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
    private Integer parentId;

    /**
     * 描述说明
     */
    private String describe;

}
