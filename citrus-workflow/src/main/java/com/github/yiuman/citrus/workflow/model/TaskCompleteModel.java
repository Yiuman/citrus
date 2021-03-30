package com.github.yiuman.citrus.workflow.model;

import java.util.Map;

/**
 * 任务完成模型，抽象完成流程任务主要属性
 *
 * @author yiuman
 * @date 2020/12/14
 */
public interface TaskCompleteModel extends ProcessPersonalModel {

    /**
     * 获取任务ID
     *
     * @return 任务ID
     */
    String getTaskId();

    /**
     * 流程变量
     *
     * @return 获取完成任务时的流程变量
     */
    Map<String, Object> getVariables();

    /**
     * 任务变量
     *
     * @return 属于任务的任务变量
     */
    Map<String, Object> getTaskVariables();

    /**
     * 获取下一步目标任务的Key，用于任务完成时任务跳转等操作
     *
     * @return 流程图定义的活动key
     * @see org.activiti.bpmn.model.Activity
     */
    String getTargetTaskKey();
}