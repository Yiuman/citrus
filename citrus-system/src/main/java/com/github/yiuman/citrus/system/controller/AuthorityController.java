package com.github.yiuman.citrus.system.controller;

import com.github.yiuman.citrus.support.crud.controller.BaseCrudController;
import com.github.yiuman.citrus.system.dto.AuthorityDto;
import com.github.yiuman.citrus.system.dto.AuthorityQuery;
import com.github.yiuman.citrus.system.service.AuthorityService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限控制器
 *
 * @author yiuman
 * @date 2020/4/4
 */
@RestController
@RequestMapping("/rest/auth")
public class AuthorityController extends BaseCrudController<AuthorityService, AuthorityDto, Long> {

    public AuthorityController() {
        setParamClass(AuthorityQuery.class);
    }

}
