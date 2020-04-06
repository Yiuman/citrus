package com.github.yiuman.citrus.rbac.service;

import com.github.yiuman.citrus.rbac.dto.OrganDto;
import com.github.yiuman.citrus.rbac.entity.Organization;
import com.github.yiuman.citrus.rbac.mapper.OrganMapper;
import com.github.yiuman.citrus.support.crud.BaseDtoCrudService;
import org.springframework.stereotype.Component;

/**
 * @author yiuman
 * @date 2020/4/6
 */
@Component
public class OrganService extends BaseDtoCrudService<OrganMapper, Organization, OrganDto, Long> {

}
