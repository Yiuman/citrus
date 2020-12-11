package com.github.yiuman.citrus.workflow.service;

import org.activiti.engine.runtime.ProcessInstance;

import java.util.Map;

/**
 * 流程服务类
 *
 * @author yiuman
 * @date 2020/12/11
 */
public interface ProcessService<F> {

    String getProcessDefinitionKey();


    /**
     * 开始一个流程
     *
     * @param form 表单数据
     * @return 流程实例
     */
    ProcessInstance starProcess(F form);

    /**
     * 获取流程需要关联的表单实例的主键
     *
     * @param form 表单实例
     * @return 主键
     */
    String getBusinessFormKey(F form);

    /**
     * 根据业务表单实例获取流程需要的变量
     *
     * @param form 表单数据
     * @return 需要的流程实例变量
     */
    Map<?, ?> getProcessInstanceVars(F form);

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