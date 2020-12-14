package com.github.yiuman.citrus.workflow.service.impl;

import com.github.yiuman.citrus.workflow.service.BaseFlowableService;
import com.github.yiuman.citrus.workflow.service.ProcessQueryService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import java.util.List;

/**
 * @author yiuman
 * @date 2020/12/14
 */
public class ProcessQueryServiceImpl extends BaseFlowableService implements ProcessQueryService {

    public ProcessQueryServiceImpl() {
    }

    @Override
    public List<ProcessInstance> getProcessInstances(ProcessInstanceQuery processInstanceQuery) {
        throw new UnsupportedOperationException("Method not implemented.");
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
