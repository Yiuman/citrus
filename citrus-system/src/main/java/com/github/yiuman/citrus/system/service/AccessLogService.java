package com.github.yiuman.citrus.system.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yiuman.citrus.support.crud.service.BaseService;
import com.github.yiuman.citrus.support.utils.WebUtils;
import com.github.yiuman.citrus.system.dto.ResourceDto;
import com.github.yiuman.citrus.system.entity.AccessLog;
import com.github.yiuman.citrus.system.entity.Resource;
import com.github.yiuman.citrus.system.entity.User;
import com.github.yiuman.citrus.system.enums.ResourceType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author yiuman
 * @date 2020/9/24
 */
@Service
public class AccessLogService extends BaseService<AccessLog, Long> {

    private final ObjectMapper objectMapper;

    private final ResourceService resourceService;

    public AccessLogService(ObjectMapper objectMapper, ResourceService resourceService) {
        this.objectMapper = objectMapper;
        this.resourceService = resourceService;
    }

    public void pointAccess(HttpServletRequest request, User user, Resource resource) throws Exception {
        AccessLog accessLog = new AccessLog();
        if (Objects.nonNull(user)) {
            accessLog.setUserId(user.getUserId());
            accessLog.setUsername(user.getUsername());
        } else {
            accessLog.setUsername("anonymous");
        }

        accessLog.setIpAddress(WebUtils.getIpAddress(request));
        accessLog.setUrl(request.getRequestURI());
        accessLog.setRequestMethod(request.getMethod());
        accessLog.setParams(objectMapper.writeValueAsString(request.getParameterMap()));
        if (Objects.nonNull(resource)) {
            accessLog.setResourceId(resource.getId());
            String resourceName = resource.getResourceName();
            if (ResourceType.OPERATION == resource.getType() && Objects.nonNull(resource.getParentId())) {
                ResourceDto resourceDto = resourceService.get(resource.getParentId());
                resourceName = resourceDto.getResourceName() + resourceName;
            }
            accessLog.setResourceType(resource.getType());
            accessLog.setResourceName(resourceName);
        } else {
            accessLog.setResourceType(ResourceType.UNKNOWN);
        }

        accessLog.setCreatedTime(LocalDateTime.now());

        save(accessLog);
    }

    public void pointAccessWithResourceName(HttpServletRequest request, User user, String resourceName) throws Exception {
        Resource resource = new Resource();
        resource.setResourceName(resourceName);
        pointAccess(request, user, resource);
    }

}
