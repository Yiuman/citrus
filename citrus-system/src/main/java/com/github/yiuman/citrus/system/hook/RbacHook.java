package com.github.yiuman.citrus.system.hook;

import com.github.yiuman.citrus.security.authorize.AuthorizeServiceHook;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import com.github.yiuman.citrus.support.utils.WebUtils;
import com.github.yiuman.citrus.system.entity.Resource;
import com.github.yiuman.citrus.system.entity.User;
import com.github.yiuman.citrus.system.service.ResourceService;
import com.github.yiuman.citrus.system.service.RoleService;
import com.github.yiuman.citrus.system.service.UserService;
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

    private final UserService userService;

    private final RoleService roleService;

    private final ResourceService resourceService;

    public RbacHook(UserService userService, RoleService roleService, ResourceService resourceService) {
        this.userService = userService;
        this.roleService = roleService;
        this.resourceService = resourceService;
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
            //获取当前请求的对应的RequestMapping路径
            String mvcDefineMapping = WebUtils.getRequestMapping(httpServletRequest);
            if (mvcDefineMapping == null) {
                return true;
            }

            //没有配置资源的情况下都可以访问
            Resource resource = resourceService.selectByUri(mvcDefineMapping, httpServletRequest.getMethod());
            if (resource == null) {
                return true;
            }

            Optional<User> user = userService.getUser(authentication);
            return user.filter(LambdaUtils.predicateWrapper(value -> roleService.hasPermission(value.getUserId(), resource.getResourceId()))).isPresent();
        } catch (Exception e) {
            log.error("RbacService Exception", e);
            return false;
        }

    }
}
