package com.github.yiuman.citrus.system.authticate;

import com.github.yiuman.citrus.support.crud.view.ActionableView;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.widget.Button;
import com.github.yiuman.citrus.support.widget.ButtonGroup;
import com.github.yiuman.citrus.support.widget.Widget;
import com.github.yiuman.citrus.system.dto.UserOnlineInfo;
import com.github.yiuman.citrus.system.entity.Resource;
import com.github.yiuman.citrus.system.enums.ResourceType;
import com.github.yiuman.citrus.system.hook.RbacHook;
import com.github.yiuman.citrus.system.service.RbacMixinService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
@RequiredArgsConstructor
public class PageAuthenticateAdvice {

    private final RbacMixinService rbacMixinService;

    @Pointcut("execution(* com.github.yiuman.citrus.support.crud.rest.QueryRestful.page(javax.servlet.http.HttpServletRequest)))")
    public void queryRestful() {

    }

    @Around("queryRestful()")
    public Page<?> returnPageObject(ProceedingJoinPoint joinPoint) throws Throwable {
        Page<?> proceed = (Page<?>) joinPoint.proceed();
        Object view = proceed.getView();
        if (view instanceof ActionableView) {
            HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[0];
            final Resource currentResource = (Resource) request.getAttribute(RbacHook.CURRENT_RESOURCE_ATTR);
            if (Objects.nonNull(currentResource)) {
                UserOnlineInfo currentUserOnlineInfo = rbacMixinService.getUserService().getCurrentUserOnlineInfo();
                rbacMixinService.setUserOwnedInfo(currentUserOnlineInfo);
                ActionableView actionableView = (ActionableView) view;
                List<Widget<?, ?>> buttons = actionableView.getButtons();
                boolean isOperationEmpty = CollectionUtils.isEmpty(buttons);
                if (currentUserOnlineInfo.getAdmin() || isOperationEmpty) {
                    return proceed;
                }

                //根据当前用户的资源查出当前请求的相关操作资源
                final Set<String> resources = currentUserOnlineInfo.getResources()
                        .parallelStream().filter(
                                resource -> resource.getType() == ResourceType.OPERATION
                                        && currentResource.getParentId().equals(resource.getParentId()))
                        .map(Resource::getResourceCode)
                        .collect(Collectors.toSet());

                final List<Widget<?, ?>> newButtons = new ArrayList<>();
                buttons.forEach(button -> filterOperation(newButtons, button, resources));

                //过滤操作资源
                actionableView.setButtons(newButtons);
            }

        }
        return proceed;
    }

    private void filterOperation(List<Widget<?, ?>> buttons, Widget<?, ?> button, Set<String> operationResources) {
        if (button instanceof ButtonGroup) {
            ButtonGroup buttonGroup = (ButtonGroup) button;
            List<Widget<?, ?>> groupActions = new ArrayList<>();
            buttonGroup.getModel().forEach(button1 -> filterOperation(groupActions, button1, operationResources));
            if (!CollectionUtils.isEmpty(groupActions)) {
                buttonGroup.setModel(groupActions.stream().map(widget -> ((Button) widget)).collect(Collectors.toList()));
                buttons.add(button);
            }
        } else {
            if (operationResources.contains(button.getKey().toLowerCase())) {
                buttons.add(button);
            }
        }
    }
}
