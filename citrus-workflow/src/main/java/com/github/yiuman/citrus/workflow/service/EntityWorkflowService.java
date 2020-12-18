package com.github.yiuman.citrus.workflow.service;

import com.github.yiuman.citrus.workflow.model.ProcessBusinessModel;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.Map;

/**
 * 实体模型的流程
 *
 * @author yiuman
 * @date 2020/12/16
 */
public interface EntityWorkflowService<E extends ProcessBusinessModel> extends WorkflowService {

    /**
     * 获取流程定义的Key
     *
     * @return 流程定义的Key
     */
    String getProcessDefineKey();

    /**
     * 根据业务实体模型启动流程
     *
     * @param entity 实体
     * @return 流程实例
     * @throws Exception 可以为数据库异常或流程相关异常
     */
    ProcessInstance starProcess(E entity) throws Exception;

    /**
     * 根据ID完成任务
     *
     * @param taskId    任务ID
     * @param variables 任务变量
     */
    void complete(String taskId, Map<String, Object> variables);

}