package com.github.yiuman.citrus.system.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yiuman.citrus.support.crud.service.BaseDtoService;
import com.github.yiuman.citrus.system.dto.ScopeDto;
import com.github.yiuman.citrus.system.entity.Scope;
import com.github.yiuman.citrus.system.mapper.ScopeMapper;
import org.springframework.stereotype.Service;

/**
 * 数据范围逻辑层
 *
 * @author yiuman
 * @date 2020/6/1
 */
@Service
public class ScopeService extends BaseDtoService<Scope, Long, ScopeDto> {

    private final ScopeMapper scopeMapper;

    public ScopeService(ScopeMapper scopeMapper) {
        this.scopeMapper = scopeMapper;
    }

    @Override
    protected BaseMapper<Scope> getBaseMapper() {
        return scopeMapper;
    }

    @Override
    public boolean beforeSave(ScopeDto entity) throws Exception {
        if (entity.getOrganId() == null) {
            entity.setOrganId(-1L);
        }
        return true;
    }
}
