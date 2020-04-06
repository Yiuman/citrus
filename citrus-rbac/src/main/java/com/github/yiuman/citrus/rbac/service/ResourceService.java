package com.github.yiuman.citrus.rbac.service;

import com.github.yiuman.citrus.rbac.dto.ResourceDto;
import com.github.yiuman.citrus.rbac.entity.Resource;
import com.github.yiuman.citrus.rbac.mapper.ResourceMapper;
import com.github.yiuman.citrus.support.crud.BaseDtoCrudService;
import org.springframework.stereotype.Service;

/**
 * 资源逻辑服务类
 *
 * @author yiuman
 * @date 2020/3/31
 */
@Service
public class ResourceService extends BaseDtoCrudService<ResourceMapper, Resource, ResourceDto, Long> {

    public ResourceService() {
    }

    public Resource selectByUri(String requestURI, String method) {
        return query().ge("path", requestURI).ge("operation", method).ge("type", 0).one();
    }
}
