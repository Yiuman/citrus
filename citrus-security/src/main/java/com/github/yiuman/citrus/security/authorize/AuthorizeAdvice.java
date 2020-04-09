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

    /**
     * 声明拦截
     * 1.方法上有使用@Authorize
     * 2.类上有使用@Authorize
     */
    @Pointcut("@annotation(com.github.yiuman.citrus.security.authorize.Authorize) " +
            "|| @within(com.github.yiuman.citrus.security.authorize.Authorize) ")
    public void authorizePointCut() {
    }

    /**
     * 拦截BaseCrudController中的所有方法
     */
    @Pointcut("execution(* com.github.yiuman.citrus.support.crud.controller.*.*(..))")
    public void crudPointCut() {

    }


    /**
     * 拦截请求的方法
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping) ")
    public void requestPointCut() {

    }

    /**
     * 组合拦截：
     * 1.方法上有使用@Authorize
     * 2.类上有使用@Authorize
     * 3.拦截BaseCrudController中的有@RequestMapping的标记的方法
     */
    @Pointcut("authorizePointCut() || (crudPointCut() && requestPointCut()))")
    public void combination() {

    }

    @Around("combination()")
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
