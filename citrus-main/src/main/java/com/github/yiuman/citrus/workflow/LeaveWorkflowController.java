package com.github.yiuman.citrus.workflow;

import com.github.yiuman.citrus.workflow.rest.EntityWorkflowController;
import com.github.yiuman.citrus.workflow.service.EntityCrudWorkflowService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 请假流程
 *
 * @author yiuman
 * @date 2020/12/21
 */
@RestController
@RequestMapping("/rest/leaves")
public class LeaveWorkflowController extends EntityWorkflowController<Leave, Long> {

    private final LeaveWorkflowService workflowService;

    public LeaveWorkflowController(LeaveWorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @Override
    protected EntityCrudWorkflowService<Leave, Long> getProcessService() {
        return workflowService;
    }

}
