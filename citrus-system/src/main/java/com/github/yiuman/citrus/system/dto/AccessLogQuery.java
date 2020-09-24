package com.github.yiuman.citrus.system.dto;

import com.github.yiuman.citrus.support.crud.query.QueryParam;
import lombok.Data;

import java.util.List;

/**
 * @author yiuman
 * @date 2020/9/24
 */
@Data
public class AccessLogQuery {

    @QueryParam
    private Long userId;

    @QueryParam(type = "in")
    private List<Long> resourceType;
}
