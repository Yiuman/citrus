package com.github.yiuman.citrus.workflow.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yiuman.citrus.workflow.model.StartProcessModel;
import com.github.yiuman.citrus.workflow.model.TaskCompleteModel;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * 流程抽象类<br/>
 * 泛型F代表开始流程的业务表单类型。
 *
 * @author yiuman
 * @date 2020/12/11
 */
public abstract class BaseProcessService extends BaseFlowableService implements ProcessService {

    @Override
    public ProcessInstance starProcess(StartProcessModel model) {
        String processDefineId = model.getProcessDefineId();
        //找到流程定义
        ProcessDefinition definition = Optional.ofNullable(
                repositoryService
                        .createProcessDefinitionQuery()
                        .processDefinitionId(processDefineId).singleResult()
        ).orElseThrow(() -> new IllegalArgumentException(String.format("can not find ProcessDefinition for key:[%s]", processDefineId)));

        //开起流程
        Map<String, Object> processInstanceVars = model.getVariables();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(
                definition.getId(),
                model.getBusinessKey(),
                processInstanceVars
        );

        //找到当前流程的任务节点。
        //若任务处理人与申请人一致，则自动完成任务，直接进入下一步
        //如请假申请为流程的第一步，则此任务自动完成
        Task applyUserTask = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .taskCandidateOrAssigned(model.getUserId())
                .active()
                .singleResult();
        if (Objects.nonNull(applyUserTask)) {
            taskService.complete(applyUserTask.getId(), processInstanceVars);
            //完成后，设置下一步的候选人或办理人
            Task nextTask = taskService.createTaskQuery()
                    .processInstanceId(processInstance.getId())
                    .active().singleResult();
            if (Objects.nonNull(nextTask)) {
                setCandidateOrAssigned(nextTask, model.getCandidateOrAssigned());
            }
        }

        return processInstance;
    }

    @Override
    public void complete(TaskCompleteModel model) {
        //扎到相关任务
        Task task = Optional.ofNullable(taskService.createTaskQuery().
                taskId(model.getTaskId())
                .active()
                .singleResult())
                .orElseThrow(() -> new RuntimeException(String.format("can not find Task for taskId:[%s]", model.getTaskId())));

        //1.找到任务的处理人，若任务未签收（没有处理人），则抛出异常
        //2.若处理人与任务模型的用户不匹配也抛出异常
        String assignee = task.getAssignee();
        Assert.notNull(assignee, String.format("Task for taskId:[%s] did not claimed", task.getId()));
        if (!assignee.equals(model.getUserId())) {
            throw new RuntimeException(String.format("Task for taskId:[%s] can not complete by user:[%s]", task.getId(), model.getUserId()));
        }
        taskService.complete(task.getId(), model.getVariables(), model.getTaskVariables());
        //完成此环节后，检查有没下个环节，有的话且是未设置办理人或候选人的情况下，使用模型进行设置
        Task nextTask = taskService.createTaskQuery()
                .processInstanceId(task.getProcessInstanceId())
                .active()
                .singleResult();

        if (Objects.nonNull(nextTask)) {
            setCandidateOrAssigned(nextTask, model.getCandidateOrAssigned());
        }

    }

    protected void setCandidateOrAssigned(Task task, Set<String> candidateOrAssigned) {
        //查询当前任务是否已经有候选人或办理人
        boolean isNextTaskWithoutHandler = CollectionUtils.isEmpty(taskService.getIdentityLinksForTask(task.getId()));
        if (isNextTaskWithoutHandler && CollectionUtils.isNotEmpty(candidateOrAssigned)) {
            //只有一个候选人直接为办理人
            if (candidateOrAssigned.size() == 1) {
                task.setAssignee(candidateOrAssigned.stream().findFirst().get());
            } else {
                taskService.addCandidateUser(task.getId(), String.join(",", candidateOrAssigned));
            }
        }
    }

    @Override
    public void claim(String taskId, String userId) {
        Task task = Optional.ofNullable(taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult())
                .orElseThrow(() -> new RuntimeException(String.format("can not find Task for taskId:[%s]", taskId)));

        String assignee = task.getAssignee();
        if (StringUtils.isNotBlank(assignee)) {
            throw new RuntimeException(String.format("Task for taskId:[%s] has been claimed", taskId));
        }

        taskService.claim(taskId, userId);
    }

    @Override
    public void suspend(String instanceId) {
        runtimeService.suspendProcessInstanceById(instanceId);
    }

    @Override
    public void activate(String instanceId) {
        runtimeService.activateProcessInstanceById(instanceId);
    }
}