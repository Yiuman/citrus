package com.github.yiuman.citrus.rbac.controller;

import com.github.yiuman.citrus.rbac.dto.OrganDto;
import com.github.yiuman.citrus.rbac.dto.OrganQuery;
import com.github.yiuman.citrus.rbac.service.OrganService;
import com.github.yiuman.citrus.support.crud.BaseCrudController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 组织机构控制器
 *
 * @author yiuman
 * @date 2020/4/6
 */
@RestController
@RequestMapping("/rest/organ")
public class OrganController extends BaseCrudController<OrganService, OrganDto, Long> {

    public OrganController() {
        setParamClass(OrganQuery.class);
    }
}
