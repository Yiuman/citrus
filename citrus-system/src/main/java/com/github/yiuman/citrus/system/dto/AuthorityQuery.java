package com.github.yiuman.citrus.system.dto;

import com.github.yiuman.citrus.support.crud.query.QueryParam;
import lombok.Data;

/**
 * @author yiuman
 * @date 2020/4/12
 */
@Data
public class AuthorityQuery {

    @QueryParam
    private String authorityName;
}
