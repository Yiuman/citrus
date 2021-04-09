package com.github.yiuman.citrus.workflow.service;

import com.github.yiuman.citrus.workflow.model.StartProcessModel;
import com.github.yiuman.citrus.workflow.model.TaskCompleteModel;
import org.activiti.engine.runtime.ProcessInstance;

/**
 * 流程服务类
 *
 * @author yiuman
 * @date 2020/12/11
 */
public interface WorkflowService extends WorkflowEngineGetter {

    /**
     * 开始一个流程
     *
     * @param model 开始流程模型
     * @return 流程实例
     * @see StartProcessModel
     */
    ProcessInstance starProcess(StartProcessModel model);

    /**
     * 完成任务
     *
     * @param model 任务完成模型
     * @see TaskCompleteModel
     */
    void complete(TaskCompleteModel model);

    /**
     * 签收任务
     *
     * @param taskId 任务ID
     * @param userId 用户ID
     */
    void claim(String taskId, String userId);

    /**
     * 任务跳转
     *  @param taskId          当前的任务
     * @param targetTaskKey 目前任务的定义key
     */
    void jump(String taskId, String targetTaskKey);

    /**
     * 挂起流程
     *
     * @param instanceId 流程实例ID
     */
    void suspend(String instanceId);

    /**
     * 激活流程
     *
     * @param instanceId 流程实例ID
     */
    void activate(String instanceId);
}