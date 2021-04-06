package com.github.yiuman.citrus.workflow;

import com.github.yiuman.citrus.support.crud.view.impl.DialogView;
import com.github.yiuman.citrus.support.crud.view.impl.PageTableView;
import com.github.yiuman.citrus.support.utils.Buttons;
import com.github.yiuman.citrus.support.widget.Inputs;
import com.github.yiuman.citrus.system.service.UserService;
import com.github.yiuman.citrus.workflow.rest.BaseEntityWorkflowController;
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
public class LeaveWorkflowControllerBase extends BaseEntityWorkflowController<Leave, Long> {

    private final LeaveWorkflowService workflowService;

    private final UserService userService;

    public LeaveWorkflowControllerBase(LeaveWorkflowService workflowService, UserService userService) {
        this.workflowService = workflowService;
        this.userService = userService;
    }

    @Override
    protected EntityCrudWorkflowService<Leave, Long> getProcessService() {
        return workflowService;
    }


    @Override
    protected Object createView() {
        PageTableView<Leave> view = new PageTableView<>();
        view.addHeader("ID", "leaveId");
        view.addHeader("请假天数", "leaveDay");
        view.addHeader("申请人", "username", entity -> userService.get(entity.getUserId()).getUsername());
        view.addHeader("流程ID", "processInstanceId");
        view.addButton(Buttons.defaultButtonsWithMore());
        view.addAction(Buttons.defaultActions());
        return view;
    }

    @Override
    protected Object createEditableView() throws Exception {
        DialogView view = new DialogView();
        view.addEditField(new Inputs("请假天数", "leaveDay").type("number")).addRule("required");
        return view;
    }

}
