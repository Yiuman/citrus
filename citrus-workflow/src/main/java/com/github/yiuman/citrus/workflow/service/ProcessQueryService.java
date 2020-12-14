package com.github.yiuman.citrus.workflow.service;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import java.util.List;

/**
 * 流程相关查询服务类
 *
 * @author yiuman
 * @date 2020/12/14
 */
public interface ProcessQueryService {


    /**
     * 根据流程定义的KEY获取所有此定义的流程实例
     *
     * @param processInstanceQuery 查询条件满足条件的
     * @return 所有的流程实例
     */
    List<ProcessInstance> getProcessInstances(ProcessInstanceQuery processInstanceQuery);

    /**
     * 获取所有的流程实例
     *
     * @param definedKey 流程定义的KEY
     * @return 流程定义的所有流程实例
     */
    List<ProcessInstance> getProcessInstancesByDefineKey(String definedKey);

    /**
     * 获取流程实例
     *
     * @param instanceId 流程实例ID
     * @return 流程实例实体
     */
    ProcessInstance getProcessInstanceById(String instanceId);

    /**
     * 获取流程实例的表单对象
     *
     * @param instanceId 流程实例ID
     * @return 表单实例
     */
    Object getInstanceBusinessObject(String instanceId);

    /**
     * 查询流程实例所有的任务
     *
     * @param taskQuery 任务查询条件
     * @return 满足条件的所有流程实例任务
     */
    List<Task> getTask(TaskQuery taskQuery);

    /**
     * 查询流程实例所有的任务
     *
     * @param instanceId 流程实例ID
     * @return 所有流程实例任务（包含在活动节点及历史完成节点）
     */
    List<Task> getTaskByInstanceId(String instanceId);

    /**
     * 查询流程实例在活动任务
     *
     * @param instanceId 流程实例ID
     * @return 活动ING的流程实例任务
     */
    List<Task> getActiveTaskByInstanceId(String instanceId);

    /**
     * 获取当前流程实例的活动中的任务（单个）
     *
     * @param instanceId 流程实例ID
     * @return 单个活动任务
     */
    Task getSingleActiveTaskByInstanceId(String instanceId);

    /**
     * 根据ID获取任务
     *
     * @param taskId 任务ID
     * @return 任务
     */
    Task getTaskById(String taskId);

    /**
     * 获取任务的表单对象
     *
     * @param taskId 任务ID
     * @return 任务进行中的表单对象
     */
    Object getTaskBusinessObject(String taskId);

}