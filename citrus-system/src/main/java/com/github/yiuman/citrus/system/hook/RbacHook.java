package com.github.yiuman.citrus.system.hook;

import com.github.yiuman.citrus.security.authorize.AuthorizeServiceHook;
import com.github.yiuman.citrus.support.utils.WebUtils;
import com.github.yiuman.citrus.system.entity.Resource;
import com.github.yiuman.citrus.system.entity.User;
import com.github.yiuman.citrus.system.service.AccessLogService;
import com.github.yiuman.citrus.system.service.RbacMixinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 鉴权服务类
 *
 * @author yiuman
 * @date 2020/3/23
 */
@Service
@Slf4j
public class RbacHook implements AuthorizeServiceHook {

    private final RbacMixinService mixinService;

    private final AccessLogService accessLogService;

    public RbacHook(RbacMixinService mixinService, AccessLogService accessLogService) {
        this.mixinService = mixinService;
        this.accessLogService = accessLogService;
    }

    /**
     * 系统级权限数据范围处理
     *
     * @param httpServletRequest 当前的请求
     * @param authentication     当前的身份认证信息
     * @return true/false
     */
    @Override
    public boolean hasPermission(HttpServletRequest httpServletRequest, Authentication authentication) {
        try {

            //没登录不给进,登录了校验权限
            Optional<User> user = mixinService.getUserService().getUser(authentication);
            //获取当前请求的对应的RequestMapping路径
            String mvcDefineMapping = WebUtils.getRequestMapping(httpServletRequest);
            if (mvcDefineMapping == null) {
                accessLogService.pointAccess(httpServletRequest, user.orElse(null), null);
                return true;
            }

            //没有配置资源的情况下都可以访问
            Resource resource = mixinService.getResourceService().selectByUri(mvcDefineMapping, httpServletRequest.getMethod());
            accessLogService.pointAccess(httpServletRequest, user.orElse(null), resource);
            if (resource == null) {
                return true;
            }

            return user.filter(value -> mixinService.hasPermission(value, resource)).isPresent();

        } catch (Exception e) {
            log.error("RbacService Exception", e);
            return false;
        }

    }

}
