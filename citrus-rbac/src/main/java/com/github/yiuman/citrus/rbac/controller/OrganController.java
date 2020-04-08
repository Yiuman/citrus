package com.github.yiuman.citrus.rbac.controller;

import com.github.yiuman.citrus.rbac.dto.OrganQuery;
import com.github.yiuman.citrus.rbac.entity.Organization;
import com.github.yiuman.citrus.rbac.service.OrganService;
import com.github.yiuman.citrus.support.crud.BaseCrudController;
import com.github.yiuman.citrus.support.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 组织机构控制器
 *
 * @author yiuman
 * @date 2020/4/6
 */
@RestController
@RequestMapping("/rest/organ")
public class OrganController extends BaseCrudController<Organization, Long> {

    public OrganController() {
        setParamClass(OrganQuery.class);
    }

    @GetMapping("/hello")
    public ResponseEntity<List<Organization>> hello() throws Exception {
        OrganService service = (OrganService) getService();
        return ResponseEntity.ok(service.getList());
    }
}
