package com.github.yiuman.citrus.system.service;

import com.github.yiuman.citrus.support.crud.mapper.TreeMapper;
import com.github.yiuman.citrus.support.crud.service.BaseTreeService;
import com.github.yiuman.citrus.system.entity.Organization;
import com.github.yiuman.citrus.system.mapper.OrganMapper;
import org.springframework.stereotype.Component;

/**
 * @author yiuman
 * @date 2020/4/6
 */
@Component
public class OrganService extends BaseTreeService<Organization, Long> {

    private final OrganMapper organMapper;

    public OrganService(OrganMapper organMapper) {
        this.organMapper = organMapper;
    }

    @Override
    protected TreeMapper<Organization> getTreeMapper() {
        return organMapper;
    }
}
