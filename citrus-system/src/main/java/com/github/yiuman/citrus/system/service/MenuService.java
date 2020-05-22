package com.github.yiuman.citrus.system.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yiuman.citrus.support.crud.service.BaseSimpleTreeService;
import com.github.yiuman.citrus.system.entity.Resource;
import com.github.yiuman.citrus.system.mapper.ResourceMapper;
import org.springframework.stereotype.Service;

/**
 * 菜单树逻辑层
 *
 * @author yiuman
 * @date 2020/5/22
 */
@Service
public class MenuService extends BaseSimpleTreeService<Resource, Long> {

    private final ResourceMapper resourceMapper;

    public MenuService(ResourceMapper resourceMapper) {
        this.resourceMapper = resourceMapper;
    }

    @Override
    protected BaseMapper<Resource> getMapper() {
        return resourceMapper;
    }
}
