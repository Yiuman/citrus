package com.github.yiuman.citrus.system.controller;

import com.github.yiuman.citrus.system.dto.UserDto;
import com.github.yiuman.citrus.system.dto.UserQuery;
import com.github.yiuman.citrus.system.hook.HasLoginHook;
import com.github.yiuman.citrus.security.authorize.Authorize;
import com.github.yiuman.citrus.support.crud.controller.BaseCrudController;
import com.github.yiuman.citrus.system.service.UserService;
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
public class UserController extends BaseCrudController<UserService,UserDto, Long> {

    public UserController() {
        setParamClass(UserQuery.class);
    }
}
