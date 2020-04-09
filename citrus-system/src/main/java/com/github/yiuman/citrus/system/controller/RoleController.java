package com.github.yiuman.citrus.system.controller;

import com.github.yiuman.citrus.system.dto.RoleDto;
import com.github.yiuman.citrus.system.dto.RoleQuery;
import com.github.yiuman.citrus.support.crud.controller.BaseCrudController;
import com.github.yiuman.citrus.system.service.RoleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色控制器
 *
 * @author yiuman
 * @date 2020/4/6
 */
@RestController
@RequestMapping("/rest/roles")
public class RoleController extends BaseCrudController<RoleService,RoleDto, Long> {

    public RoleController() {
        setParamClass(RoleQuery.class);
    }


}
