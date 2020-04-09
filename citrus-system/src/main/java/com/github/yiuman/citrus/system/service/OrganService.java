package com.github.yiuman.citrus.system.service;

import com.github.yiuman.citrus.system.entity.Organization;
import com.github.yiuman.citrus.system.mapper.OrganMapper;
import com.github.yiuman.citrus.support.crud.service.BaseTreeCrudService;
import org.springframework.stereotype.Component;

/**
 * @author yiuman
 * @date 2020/4/6
 */
@Component
public class OrganService extends BaseTreeCrudService<OrganMapper, Organization, Long> {

}
