package com.github.yiuman.citrus.workflow.model;

/**
 * 流程业务接口
 * 用于定义是一个流程业务类型
 *
 * @author yiuman
 * @date 2020/12/14
 */
public interface ProcessBusinessModel {

    /**
     * 获取流程实例ID
     *
     * @return 流程实体ID
     */
    String getProcessInstanceId();

    /**
     * 设置流程实例ID
     *
     * @param instanceId 流程实例ID
     */
    void setProcessInstanceId(String instanceId);

}