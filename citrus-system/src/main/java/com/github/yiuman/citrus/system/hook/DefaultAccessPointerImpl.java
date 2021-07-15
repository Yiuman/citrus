package com.github.yiuman.citrus.system.hook;

import com.github.yiuman.citrus.system.entity.Resource;
import com.github.yiuman.citrus.system.entity.User;
import com.github.yiuman.citrus.system.service.AccessLogService;

import javax.servlet.http.HttpServletRequest;

/**
 * 默认的访问埋点实现
 *
 * @author yiuman
 * @date 2021/7/15
 */
public class DefaultAccessPointerImpl implements AccessPointer {

    private final AccessLogService accessLogService;

    public DefaultAccessPointerImpl(AccessLogService accessLogService) {
        this.accessLogService = accessLogService;
    }

    @Override
    public void doPoint(HttpServletRequest request, User user, Resource resource) {
        try {
            accessLogService.pointAccess(request, user, resource);
        } catch (Exception ignore) {
        }

    }
}
