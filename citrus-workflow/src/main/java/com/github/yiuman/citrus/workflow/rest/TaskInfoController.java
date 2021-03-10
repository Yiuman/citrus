package com.github.yiuman.citrus.workflow.rest;

import com.github.yiuman.citrus.support.crud.view.impl.PageTableView;
import lombok.Data;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * 任务查询控制器
 *
 * @author yiuman
 * @date 2021/3/8
 */
@RestController
@RequestMapping("/rest/taskinst")
public class TaskInfoController extends BaseWorkflowQueryController<Task, String> {

    public TaskInfoController() {
        setParamClass(TaskQueryParams.class);
    }

    @Data
    static class TaskQueryParams {
        String taskCandidateOrAssigned;
    }

    @Override
    protected Object createView() {
        PageTableView<Task> view = new PageTableView<>();
        view.addWidget("处理人或候选人", "taskCandidateOrAssigned");
        view.addHeader("任务名称", "name");
        view.addHeader("处理人", "assignee");
        view.addHeader("创建时间", "createTime");
        view.addHeader("处理时间", "dueDate");
        return view;
    }

    @Data
    static class TaskInfo implements Task {
        private String id;
        private String owner;
        private int assigneeUpdatedCount; // needed for v5 compatibility
        private String originalAssignee; // needed for v5 compatibility
        private String assignee;
        private DelegationState delegationState;
        private String parentTaskId;
        private String name;
        private String localizedName;
        private String description;
        private String localizedDescription;
        private int priority = DEFAULT_PRIORITY;
        private Date createTime; // The time when the task has been created
        private Date dueDate;
        private int suspensionState;
        private String category;
        private boolean isIdentityLinksInitialized;
        private String executionId;
        private String processInstanceId;
        private String processDefinitionId;
        private String taskDefinitionKey;
        private String formKey;
        private boolean isDeleted;
        private boolean isCanceled;
        private String eventName;
        private String tenantId;
        private boolean forcedUpdate;
        private boolean suspended;
        private Date claimTime;
        private Integer appVersion;
        private String businessKey;

        @Override
        public Map<String, Object> getTaskLocalVariables() {
            return null;
        }

        @Override
        public Map<String, Object> getProcessVariables() {
            return null;
        }

        static TaskInfo copy(Task task) {
            TaskInfo taskInfo = new TaskInfo();
            BeanUtils.copyProperties(task, taskInfo);
            return taskInfo;
        }
    }

    @Override
    protected Function<Task, ? extends Task> getTransformFunc() {
        return TaskInfo::copy;
    }

}
