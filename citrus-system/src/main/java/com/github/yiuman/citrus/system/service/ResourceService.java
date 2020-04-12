package com.github.yiuman.citrus.system.service;

import com.github.yiuman.citrus.support.crud.service.BaseDtoCrudService;
import com.github.yiuman.citrus.system.dto.ResourceDto;
import com.github.yiuman.citrus.system.entity.Resource;
import com.github.yiuman.citrus.system.mapper.ResourceMapper;
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
public class ResourceService extends BaseDtoCrudService<ResourceMapper, Resource, ResourceDto, Long> {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public ResourceService(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    public Resource selectByUri(String requestURI, String method) {
        return query().ge("path", requestURI).ge("operation", method).ge("type", 0).one();
    }

    /**
     * 保存实体后查看是否是继承BaseCrudController的 若是则自动生成增删改查的操作权限
     *
     * @param entity 当前实体
     * @throws Exception 数据库操作异常
     */
    @Override
    public void afterSave(ResourceDto entity) throws Exception {
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
