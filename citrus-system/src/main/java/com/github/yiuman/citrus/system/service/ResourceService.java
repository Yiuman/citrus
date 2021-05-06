package com.github.yiuman.citrus.system.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.support.crud.service.BaseDtoService;
import com.github.yiuman.citrus.system.dto.ResourceDto;
import com.github.yiuman.citrus.system.entity.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 资源逻辑服务类
 *
 * @author yiuman
 * @date 2020/3/31
 */
@Service
public class ResourceService extends BaseDtoService<Resource, Long, ResourceDto> {

    public ResourceService() {
    }

    public Resource selectByUri(String requestUri, String method) {
        List<Resource> resources = getBaseMapper()
                .selectList(Wrappers.<Resource>lambdaQuery()
                        .eq(Resource::getPath, requestUri).eq(Resource::getOperation, method));
        return CollectionUtils.isEmpty(resources) ? null : resources.get(0);
    }

    public Resource selectByCode(String code) {
        return getBaseMapper().selectOne(Wrappers.<Resource>lambdaQuery().eq(Resource::getResourceCode, code));
    }
}
