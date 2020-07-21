package com.github.yiuman.citrus.system.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.support.crud.service.BaseDtoService;
import com.github.yiuman.citrus.system.dto.ResourceDto;
import com.github.yiuman.citrus.system.entity.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.PathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;
import java.util.Set;

/**
 * 资源逻辑服务类
 *
 * @author yiuman
 * @date 2020/3/31
 */
@Service
public class ResourceService extends BaseDtoService<Resource, Long, ResourceDto> {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public ResourceService(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    public Resource selectByUri(String requestUri, String method) {
        return getBaseMapper().selectOne(Wrappers.<Resource>query().eq("path", requestUri).eq("operation", method));
    }

    /**
     * 保存实体后查看是否是继承BaseCrudController的 若是则自动生成增删改查的操作权限
     *
     * @param entity 当前实体
     */
    @Override
    public void afterSave(ResourceDto entity) {
        PathMatcher pathMatcher = requestMappingHandlerMapping.getPathMatcher();
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        handlerMethods.forEach((key, value) -> {
            PatternsRequestCondition patternsCondition = key.getPatternsCondition();
            Set<String> patterns = patternsCondition.getPatterns();
            if (patterns.stream().anyMatch(patter -> pathMatcher.isPattern(entity.getPath()))) {

            }
        });
    }

}
