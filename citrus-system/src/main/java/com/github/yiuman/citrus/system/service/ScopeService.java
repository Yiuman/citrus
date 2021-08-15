package com.github.yiuman.citrus.system.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.support.crud.query.Query;
import com.github.yiuman.citrus.support.crud.service.BaseDtoService;
import com.github.yiuman.citrus.system.dto.ScopeDto;
import com.github.yiuman.citrus.system.entity.Scope;
import com.github.yiuman.citrus.system.entity.ScopeDefine;
import com.github.yiuman.citrus.system.mapper.ScopeDefineMapper;
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

    private final ScopeDefineMapper scopeDefineMapper;

    public ScopeService(ScopeDefineMapper scopeDefineMapper) {
        this.scopeDefineMapper = scopeDefineMapper;
    }

    @Override
    public <P extends IPage<ScopeDto>> P page(P page, Query query) {
        P returnPage = super.page(page, query);
        returnPage.getRecords().forEach(item -> item.setScopeDefines(scopeDefineMapper.selectList(Wrappers.<ScopeDefine>query().eq(getKeyColumn(), item.getScopeId()))));
        return returnPage;
    }

    @Override
    public boolean beforeSave(ScopeDto entity) {
        if (entity.getOrganId() == null) {
            entity.setOrganId(-1L);
        }
        return true;
    }

    @Override
    public void afterSave(ScopeDto entity) {
        //删除旧的
        scopeDefineMapper.delete(Wrappers.<ScopeDefine>lambdaQuery().eq(ScopeDefine::getScopeId, entity.getScopeId()));

        if (!CollectionUtils.isEmpty(entity.getScopeDefines())) {
            //将当前的数据范围ID，设置到数据范围定义的关联数据范围ID中
            entity.getScopeDefines()
                    .parallelStream()
                    .forEach(scopeDefine -> scopeDefine.setScopeId(entity.getScopeId()));
            scopeDefineMapper.saveBatch(entity.getScopeDefines());
        }
    }

    public Scope getDataScopeByResourceId(Long resourceId) {
        return scopeDefineMapper.getScopeByResourceId(resourceId);
    }

    public List<ScopeDefine> getScopeDefinesByResourceId(Long resourceId) {
        return scopeDefineMapper.getScopeDefinesByResourceId(resourceId);
    }
}
