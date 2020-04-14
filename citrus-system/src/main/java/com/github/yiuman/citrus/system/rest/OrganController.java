package com.github.yiuman.citrus.system.rest;

import com.github.yiuman.citrus.support.crud.rest.BaseTreeController;
import com.github.yiuman.citrus.support.http.ResponseEntity;
import com.github.yiuman.citrus.system.dto.OrganQuery;
import com.github.yiuman.citrus.system.entity.Organization;
import com.github.yiuman.citrus.system.service.OrganService;
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
public class OrganController extends BaseTreeController<OrganService, Organization, Long> {

    public OrganController() {
        setParamClass(OrganQuery.class);
        setLazy(false);
    }

    @GetMapping("/hello")
    public ResponseEntity<List<Organization>> hello() throws Exception {
        return ResponseEntity.ok(getService().getList());
    }
}
