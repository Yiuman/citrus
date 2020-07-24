package com.github.yiuman.citrus.system.dto;

import com.github.yiuman.citrus.support.crud.query.QueryParam;
import lombok.Data;

/**
 * @author yiuman
 * @date 2020/4/6
 */
@Data
public class OrganQuery {

    @QueryParam(type = "like")
    private String organName;

}
