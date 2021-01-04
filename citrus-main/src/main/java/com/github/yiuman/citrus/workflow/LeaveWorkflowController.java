package com.github.yiuman.citrus.workflow;

import com.github.yiuman.citrus.support.model.DialogView;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.utils.Buttons;
import com.github.yiuman.citrus.support.widget.Inputs;
import com.github.yiuman.citrus.system.service.UserService;
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

    private final UserService userService;

    public LeaveWorkflowController(LeaveWorkflowService workflowService, UserService userService) {
        this.workflowService = workflowService;
        this.userService = userService;
    }

    @Override
    protected EntityCrudWorkflowService<Leave, Long> getProcessService() {
        return workflowService;
    }


    @Override
    protected Page<Leave> createPage() throws Exception {
        Page<Leave> page = super.createPage();
        page.addHeader("ID", "leaveId");
        page.addHeader("请假天数", "leaveDay");
        page.addHeader("申请人", "username", entity -> userService.get(entity.getUserId()).getUsername());
        page.addHeader("流程ID", "processInstanceId");
        page.addButton(Buttons.defaultButtonsWithMore());
        page.addActions(Buttons.defaultActions());
        return page;
    }

    @Override
    protected DialogView createDialogView() {
        DialogView view = new DialogView();
        view.addEditField(new Inputs("请假天数","leaveDay").type("number")).addRule("required");
        return view;
    }
}
