package com.github.yiuman.citrus.rbac.dto;

import com.github.yiuman.citrus.support.crud.QueryParam;
import lombok.Data;

/**
 * @author yiuman
 * @date 2020/4/6
 */
@Data
public class UserQuery {

    @QueryParam
    private String username;

}
