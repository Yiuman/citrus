package com.github.yiuman.citrus.system.rest;

import com.github.yiuman.citrus.support.crud.rest.BaseTreeController;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.crud.service.TreeService;
import com.github.yiuman.citrus.support.http.ResponseEntity;
import com.github.yiuman.citrus.system.dto.OrganQuery;
import com.github.yiuman.citrus.system.entity.Organization;
import com.github.yiuman.citrus.system.service.OrganService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class OrganController extends BaseTreeController<Organization, Long> {

    private final OrganService organService;

    public OrganController(OrganService organService) {
        setParamClass(OrganQuery.class);
        setLazy(false);
        this.organService = organService;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <S extends CrudService<Organization, Long> & TreeService<Organization, Long>> S getCrudService() {
        return (S) organService;
    }


}
