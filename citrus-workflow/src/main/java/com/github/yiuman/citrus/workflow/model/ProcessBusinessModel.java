package com.github.yiuman.citrus.workflow.model;

/**
 * 流程业务接口
 * 用于定义是一个流程业务类型
 *
 * @author yiuman
 * @date 2020/12/14
 */
public interface ProcessBusinessModel {

    String getInstanceId();

    void setInstanceId(String instanceId);

}