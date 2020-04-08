package com.github.yiuman.citrus.rbac.controller;

import com.github.yiuman.citrus.rbac.dto.UserDto;
import com.github.yiuman.citrus.rbac.dto.UserQuery;
import com.github.yiuman.citrus.rbac.hook.HasLoginHook;
import com.github.yiuman.citrus.security.authorize.Authorize;
import com.github.yiuman.citrus.support.crud.BaseCrudController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制层
 *
 * @author yiuman
 * @date 2020/3/30
 */
@RestController
@RequestMapping("/rest/users")
@Authorize(HasLoginHook.class)
public class UserController extends BaseCrudController<UserDto, Long> {

    public UserController() {
        setParamClass(UserQuery.class);
    }
}
