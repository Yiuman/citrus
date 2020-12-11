package com.github.yiuman.citrus.workflow.service;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.Map;

/**
 * 流程任务逻辑服务类
 *
 * @author yiuman
 * @date 2020/12/11
 */
public interface TaskService {

    /**
     * 获取当前任务的流程实例
     *
     * @param task 当前任务实例
     * @return 流程实例
     */
    ProcessInstance getProcessInstance(Task task);

    /**
     * 根据任务ID获取流程实例
     *
     * @param taskId 任务ID
     * @return 流程实例
     */
    ProcessInstance getProcessInstance(String taskId);

    Map<String, Object> getTaskVariables(String taskId);

}