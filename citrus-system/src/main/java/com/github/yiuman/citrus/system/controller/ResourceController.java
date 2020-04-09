package com.github.yiuman.citrus.system.controller;

import com.github.yiuman.citrus.system.dto.ResourceDto;
import com.github.yiuman.citrus.support.crud.controller.BaseCrudController;
import com.github.yiuman.citrus.system.service.ResourceService;
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
public class ResourceController extends BaseCrudController<ResourceService,ResourceDto, Long> {

}
