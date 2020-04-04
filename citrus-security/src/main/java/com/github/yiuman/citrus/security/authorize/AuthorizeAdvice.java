package com.github.yiuman.citrus.security.authorize;

import com.github.yiuman.citrus.security.authenticate.NoPermissionException;
import com.github.yiuman.citrus.support.utils.SpringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * 权限切面,通过权限钩子实现类验证权限
 *
 * @author yiuman
 * @date 2020/4/4
 */
@Aspect
@Component
public class AuthorizeAdvice {


    @Pointcut("@annotation(com.github.yiuman.citrus.security.authorize.Authorize) " +
            "|| @within(com.github.yiuman.citrus.security.authorize.Authorize)")
    public void authorizePointCut() {
    }

    @Around("authorizePointCut()")
    public Object hasPermission(ProceedingJoinPoint point) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        Authorize authorize = Optional.ofNullable(method.getAnnotation(Authorize.class))
                .orElse(point.getTarget().getClass().getAnnotation(Authorize.class));
        if (authorize != null) {
            Class<? extends AuthorizeHook> hookClass = authorize.value();
            AuthorizeHook hook = SpringUtils.getBean(hookClass);
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            boolean hasPermission = hook.hasPermission(request, SecurityContextHolder.getContext().getAuthentication());
            if (!hasPermission) {
                throw new NoPermissionException();
            }
        }

        return point.proceed();
    }

}
