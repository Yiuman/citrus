package com.github.yiuman.citrus.workflow.service.impl;

import com.github.yiuman.citrus.workflow.service.WorkflowQueryService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import java.util.List;

/**
 * @author yiuman
 * @date 2020/12/14
 */
public class WorkflowQueryServiceImpl extends DefaultWorkflowEngineGetterImpl implements WorkflowQueryService {

    public WorkflowQueryServiceImpl() {
    }

    @Override
    public List<ProcessInstance> getProcessInstances(ProcessInstanceQuery processInstanceQuery) {
        return processInstanceQuery.list();
    }

    @Override
    public List<ProcessInstance> getProcessInstancesByDefineKey(String definedKey) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public ProcessInstance getProcessInstanceById(String instanceId) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public Object getInstanceBusinessObject(String instanceId) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public List<Task> getTask(TaskQuery taskQuery) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public List<Task> getTaskByInstanceId(String instanceId) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public List<Task> getActiveTaskByInstanceId(String instanceId) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public Task getSingleActiveTaskByInstanceId(String instanceId) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public Task getTaskById(String taskId) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public Object getTaskBusinessObject(String taskId) {
        throw new UnsupportedOperationException("Method not implemented.");
    }
}
