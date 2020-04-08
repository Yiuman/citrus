package com.github.yiuman.citrus.rbac.controller;

import com.github.yiuman.citrus.rbac.dto.ResourceDto;
import com.github.yiuman.citrus.support.crud.BaseCrudController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 资源控制器
 *
 * @author yiuman
 * @date 2020/4/6
 */
@RestController
@RequestMapping("/rest/resources")
public class ResourceController extends BaseCrudController<ResourceDto, Long> {

}
