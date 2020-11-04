package com.github.yiuman.citrus.system.authticate;

import com.github.yiuman.citrus.support.model.Button;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.system.dto.UserOnlineInfo;
import com.github.yiuman.citrus.system.entity.Resource;
import com.github.yiuman.citrus.system.hook.RbacHook;
import com.github.yiuman.citrus.system.service.RbacMixinService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 页面操作权限验证环绕
 *
 * @author yiuman
 * @date 2020/11/4
 */
@Aspect
@Component
public class PageAuthenticateAdvice {

    private final RbacMixinService rbacMixinService;

    public PageAuthenticateAdvice(RbacMixinService rbacMixinService) {
        this.rbacMixinService = rbacMixinService;
    }

    @Pointcut("execution(* com.github.yiuman.citrus.support.crud.rest.QueryRestful.page(javax.servlet.http.HttpServletRequest)))")
    public void queryRestful() {

    }

    @Around("queryRestful()")
    public Page<?> returnPageObject(ProceedingJoinPoint joinPoint) throws Throwable {
        UserOnlineInfo currentUserOnlineInfo = rbacMixinService.getUserService().getCurrentUserOnlineInfo();
        Page<?> proceed = (Page<?>) joinPoint.proceed();
        boolean isOperationEmpty = CollectionUtils.isEmpty(proceed.getButtons()) && CollectionUtils.isEmpty(proceed.getActions());
        if (currentUserOnlineInfo.getAdmin()
                || isOperationEmpty) {
            return proceed;
        }

        HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[0];
        final Resource currentResource = (Resource) request.getAttribute(RbacHook.CURRENT_RESOURCE_ATTR);
        //根据当前用户的资源查出当前请求的相关操作资源


        final Set<String> resources = currentUserOnlineInfo.getResources()
                .parallelStream().filter(resource -> resource.getType() == 2 && currentResource.getParentId().equals(resource.getParentId()))
                .map(Resource::getResourceCode)
                .collect(Collectors.toSet());


        final List<Button> buttons = new ArrayList<>();
        proceed.getButtons().forEach(button -> {
            filterOperation(buttons, button, resources);
        });


        //过滤操作资源
        proceed.setButtons(buttons);
        proceed.setActions(proceed.getActions().stream().filter(action -> resources.contains(action.getAction())).collect(Collectors.toList()));
        return proceed;
    }

    private void filterOperation(List<Button> buttons, Button button, Set<String> operationResources) {
        if (button.isGroup()) {
            List<Button> groupActions = new ArrayList<>();
            button.getActions().forEach(button1 -> {
                filterOperation(groupActions, button1, operationResources);
            });
            if (!CollectionUtils.isEmpty(groupActions)) {
                button.setActions(groupActions);
                buttons.add(button);
            }

        } else {
            if (operationResources.contains(button.getAction())) {
                buttons.add(button);
            }
        }


    }
}
