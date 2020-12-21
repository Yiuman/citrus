package com.github.yiuman.citrus.workflow;

import com.github.yiuman.citrus.system.service.UserService;
import com.github.yiuman.citrus.workflow.service.impl.BaseEntityWorkflowService;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author yiuman
 * @date 2020/12/21
 */
@Service
public class LeaveWorkflowService extends BaseEntityWorkflowService<Leave, Long> {

    private final UserService userService;

    public LeaveWorkflowService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected String getCurrentUserId() {
        return userService.getCurrentUserOnlineInfo().getUuid();
    }

    @Override
    public Long save(Leave entity) throws Exception {
        if (Objects.isNull(entity.getUserId())) {
            entity.setUserId(userService.getCurrentUserOnlineInfo().getUserId());
        }
        return super.save(entity);
    }
}
