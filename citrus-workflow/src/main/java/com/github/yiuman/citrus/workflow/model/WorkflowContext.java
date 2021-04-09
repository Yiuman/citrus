package com.github.yiuman.citrus.workflow.model;

import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

/**
 * 流程上下文
 *
 * @author yiuman
 * @date 2020/12/29
 */
public interface WorkflowContext {

    /**
     * 获取流程引擎
     *
     * @return 流程引擎
     */
    ProcessEngine getProcessEngine();

    /**
     * 当前流程实例
     *
     * @return 流程实例
     */
    ProcessInstance getProcessInstance();

    /**
     * 当前的流程节点
     *
     * @return 流程节点
     */
    FlowElement getFlowElement();


    /**
     * 当前执行实例的ID
     *
     * @return 执行实例ID
     */
    String getExecutionId();

    /**
     * 当前任务
     *
     * @return 当前任务信息
     */
    Task getTask();

    /**
     * 当前用户ID
     *
     * @return 用户ID字符串
     */
    String getCurrentUserId();


}