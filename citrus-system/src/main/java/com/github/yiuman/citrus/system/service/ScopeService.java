package com.github.yiuman.citrus.system.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.support.crud.service.BaseDtoService;
import com.github.yiuman.citrus.system.dto.ScopeDto;
import com.github.yiuman.citrus.system.entity.Scope;
import com.github.yiuman.citrus.system.entity.ScopeDefine;
import com.github.yiuman.citrus.system.mapper.ScopeDefineMapper;
import com.github.yiuman.citrus.system.mapper.ScopeMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 数据范围逻辑层
 *
 * @author yiuman
 * @date 2020/6/1
 */
@Service
public class ScopeService extends BaseDtoService<Scope, Long, ScopeDto> {

    private final ScopeMapper scopeMapper;

    private final ScopeDefineMapper scopeDefineMapper;

    public ScopeService(ScopeMapper scopeMapper, ScopeDefineMapper scopeDefineMapper) {
        this.scopeMapper = scopeMapper;
        this.scopeDefineMapper = scopeDefineMapper;
    }

    @Override
    public <P extends IPage<ScopeDto>> P page(P page, Wrapper<ScopeDto> queryWrapper) {
        P returnPage = super.page(page, queryWrapper);
        returnPage.getRecords().forEach(item -> item.setScopeDefines(scopeDefineMapper.selectList(Wrappers.<ScopeDefine>query().eq(getKeyColumn(), item.getScopeId()))));
        return returnPage;
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

    @Override
    public void afterSave(ScopeDto entity) throws Exception {
        if (!CollectionUtils.isEmpty(entity.getScopeDefines())) {
            scopeDefineMapper.saveBatch(entity.getScopeDefines());
        }
    }
}
