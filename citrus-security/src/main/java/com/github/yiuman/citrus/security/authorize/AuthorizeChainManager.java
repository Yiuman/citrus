package com.github.yiuman.citrus.security.authorize;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 授权链管理器
 *
 * @author yiuman
 * @date 2020/3/30
 */
@Component("authorizeChainManager")
public class AuthorizeChainManager {

    private final List<? extends AuthorizeServiceHook> authorizeHooks;

    public AuthorizeChainManager(List<? extends AuthorizeServiceHook> authorizeHooks) {
        this.authorizeHooks = authorizeHooks;
    }

    public boolean hasPermission(final HttpServletRequest httpServletRequest, final Authentication authentication) {
        final AtomicReference<Boolean> access = new AtomicReference<>(true);
        if (!CollectionUtils.isEmpty(authorizeHooks)) {
            authorizeHooks.forEach(authorizeHook -> access.set(access.get()
                    && authorizeHook.hasPermission(httpServletRequest, authentication)));
        }

        return access.get();

    }
}
