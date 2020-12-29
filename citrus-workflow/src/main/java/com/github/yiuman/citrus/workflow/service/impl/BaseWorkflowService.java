package com.github.yiuman.citrus.workflow.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yiuman.citrus.support.utils.SpringUtils;
import com.github.yiuman.citrus.workflow.exception.WorkflowException;
import com.github.yiuman.citrus.workflow.model.ProcessPersonalModel;
import com.github.yiuman.citrus.workflow.model.StartProcessModel;
import com.github.yiuman.citrus.workflow.model.TaskCompleteModel;
import com.github.yiuman.citrus.workflow.model.impl.WorkflowContextImpl;
import com.github.yiuman.citrus.workflow.resolver.TaskCandidateResolver;
import com.github.yiuman.citrus.workflow.service.WorkflowEngineGetter;
import com.github.yiuman.citrus.workflow.service.WorkflowService;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.util.Assert;

import java.util.*;

/**
 * 流程处理抽象类<br/>
 * 开启流程、完成任务、签收任务、挂起、激活
 *
 * @author yiuman
 * @date 2020/12/11
 */
public abstract class BaseWorkflowService implements WorkflowService {

    private WorkflowEngineGetter workflowEngineGetter;

    private TaskCandidateResolver taskCandidateResolver;

    @Override
    public ProcessEngine getProcessEngine() {
        workflowEngineGetter = Optional.ofNullable(workflowEngineGetter)
                .orElse(new DefaultWorkflowEngineGetterImpl());
        return workflowEngineGetter.getProcessEngine();
    }

    public TaskCandidateResolver getTaskCandidateResolver() {
        return taskCandidateResolver = Optional.ofNullable(taskCandidateResolver)
                .orElse(SpringUtils.getBean(TaskCandidateResolver.class, true));
    }

    @Override
    public ProcessInstance starProcess(StartProcessModel model) {
        String processDefineId = model.getProcessDefineKey();
        //找到流程定义
        ProcessDefinition definition = Optional.ofNullable(
                getProcessEngine().getRepositoryService()
                        .createProcessDefinitionQuery()
                        .processDefinitionKey(processDefineId)
                        .latestVersion()
                        .singleResult()
        ).orElseThrow(() -> new IllegalArgumentException(String.format("can not find ProcessDefinition for key:[%s]", processDefineId)));

        //开起流程
        Map<String, Object> processInstanceVars = model.getVariables();
        ProcessInstance processInstance = getProcessEngine().getRuntimeService().startProcessInstanceById(
                definition.getId(),
                model.getBusinessKey(),
                processInstanceVars
        );

        //找到当前流程的任务节点。
        //若任务处理人与申请人一致，则自动完成任务，直接进入下一步
        //如请假申请为流程的第一步，则此任务自动完成
        TaskService taskService = getProcessEngine().getTaskService();
        Task applyUserTask = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .taskCandidateOrAssigned(model.getUserId())
                .active()
                .singleResult();
        if (Objects.nonNull(applyUserTask)) {
            taskService.complete(applyUserTask.getId(), processInstanceVars);
            //完成后，设置下一步的候选人或办理人
            Optional.ofNullable(
                    taskService.createTaskQuery()
                            .processInstanceId(processInstance.getId())
                            .active().singleResult()
            ).ifPresent(nextTask -> setCandidateOrAssigned(nextTask, model));
        }

        return processInstance;
    }

    @Override
    public void complete(TaskCompleteModel model) {
        Assert.notNull(model.getTaskId(), "The taskId of the process can not be empty!");
        TaskService taskService = getProcessEngine().getTaskService();
        //扎到相关任务
        Task task = Optional.ofNullable(taskService.createTaskQuery().
                taskId(model.getTaskId())
                .active()
                .singleResult())
                .orElseThrow(() -> new WorkflowException(String.format("can not find Task for taskId:[%s]", model.getTaskId())));

        //1.找到任务的处理人，若任务未签收（没有处理人），则抛出异常
        //2.若处理人与任务模型的用户不匹配也抛出异常
        String assignee = task.getAssignee();
        Assert.notNull(assignee, String.format("Task for taskId:[%s] did not claimed", task.getId()));
        if (!assignee.equals(model.getUserId())) {
            throw new WorkflowException(String.format("Task for taskId:[%s] can not complete by user:[%s]", task.getId(), model.getUserId()));
        }
        taskService.complete(task.getId(), model.getVariables(), model.getTaskVariables());
        //完成此环节后，检查有没下个环节，有的话且是未设置办理人或候选人的情况下，使用模型进行设置
        Task nextTask = taskService.createTaskQuery()
                .processInstanceId(task.getProcessInstanceId())
                .active()
                .singleResult();

        if (Objects.nonNull(nextTask)) {
            setCandidateOrAssigned(nextTask, model);
        }

    }

    protected void setCandidateOrAssigned(Task task, ProcessPersonalModel model) {
        task.getProcessInstanceId();
        TaskService taskService = getProcessEngine().getTaskService();

        //查询当前任务是否已经有候选人或办理人
        RepositoryService repositoryService = getProcessEngine().getRepositoryService();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        FlowElement flowElement = bpmnModel.getFlowElement(task.getTaskDefinitionKey());
        if (flowElement instanceof UserTask) {
            UserTask userTask = (UserTask) flowElement;
            List<String> taskCandidateUsersDefine = userTask.getCandidateUsers();

            //没有负责人，则用解析器解析流程任务定义的候选人或参数传入来的候选人
            if (StringUtils.isBlank(task.getAssignee())) {
                List<String> allCandidateOrAssigned = new ArrayList<>();
                allCandidateOrAssigned.addAll(model.getCandidateOrAssigned());
                allCandidateOrAssigned.addAll(taskCandidateUsersDefine);

                //删除任务候选人
                allCandidateOrAssigned.forEach(candidateDefine -> taskService.deleteCandidateUser(task.getId(), candidateDefine));

                RuntimeService runtimeService = getProcessEngine().getRuntimeService();

                WorkflowContextImpl workflowContext = WorkflowContextImpl.builder()
                        .processInstance(
                                runtimeService
                                        .createProcessInstanceQuery()
                                        .processInstanceId(task.getProcessInstanceId())
                                        .singleResult()
                        )
                        .task(task)
                        .flowElement(flowElement)
                        .currentUserId(model.getUserId())
                        .build();
                //解析器解析完成后，把真正的候选人添加到任务中去
                Optional.ofNullable(getTaskCandidateResolver().resolve(workflowContext, allCandidateOrAssigned))
                        .ifPresent(resolvedCandidates -> {
                            if (resolvedCandidates.size() == 1) {
                                taskService.setAssignee(task.getId(), resolvedCandidates.get(1));
                            } else {
                                resolvedCandidates.forEach(realUserId -> taskService.addCandidateUser(task.getId(), realUserId));
                            }
                        });
            }
        }


    }

    @Override
    public void claim(String taskId, String userId) {
        TaskService taskService = getProcessEngine().getTaskService();
        Task task = Optional.ofNullable(taskService.createTaskQuery()
                .taskId(taskId)
                .taskCandidateOrAssigned(userId)
                .singleResult())
                .orElseThrow(() -> new WorkflowException(String.format("can not claim Task for taskId:[%s]", taskId)));

        String assignee = task.getAssignee();
        if (StringUtils.isNotBlank(assignee)) {
            throw new WorkflowException(String.format("Task for taskId:[%s] has been claimed", taskId));
        }

        taskService.claim(taskId, userId);
    }

    @Override
    public void suspend(String instanceId) {
        getProcessEngine().getRuntimeService().suspendProcessInstanceById(instanceId);
    }

    @Override
    public void activate(String instanceId) {
        getProcessEngine().getRuntimeService().activateProcessInstanceById(instanceId);
    }

}