package com.github.yiuman.citrus.workflow;

import com.github.yiuman.citrus.support.crud.query.builder.QueryBuilders;
import com.github.yiuman.citrus.support.crud.view.impl.PageTableView;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.utils.Buttons;
import com.github.yiuman.citrus.system.dto.UserDto;
import com.github.yiuman.citrus.system.entity.User;
import com.github.yiuman.citrus.system.service.UserService;
import com.github.yiuman.citrus.workflow.rest.BaseEntityWorkflowController;
import com.github.yiuman.citrus.workflow.service.EntityCrudWorkflowService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    public Object showPageView(Page<Leave> page) {
        List<Leave> records = page.getRecords();
        Set<Long> userIds = records.stream().map(Leave::getUserId).collect(Collectors.toSet());
        Map<Long, String> usernameMap = userService.list(QueryBuilders.<User>lambda().in(User::getUserId, userIds).toQuery())
                .stream().collect(Collectors.toMap(UserDto::getUserId, UserDto::getUsername));

        PageTableView<Leave> view = new PageTableView<>();
        view.addColumn("ID", "leaveId");
        view.addColumn("请假天数", "leaveDay");
        view.addColumn("申请人", "username", entity -> usernameMap.get(entity.getUserId()));
        view.addColumn("流程ID", "processInstanceId");
        view.addButton(Buttons.defaultButtonsWithMore());
//        view.addAction(Buttons.defaultActions());
        return view;
    }

//    @Override
//    protected Object createEditableView() {
//        DialogView view = new DialogView();
//        view.addEditField(new Inputs("请假天数", "leaveDay").type("number")).addRule("required");
//        return view;
//    }

}
