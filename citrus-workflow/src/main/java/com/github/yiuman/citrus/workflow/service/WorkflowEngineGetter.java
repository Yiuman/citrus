package com.github.yiuman.citrus.workflow.service;

import org.activiti.engine.ProcessEngine;

/**
 * 流程引擎获取
 *
 * @author yiuman
 * @date 2020/12/17
 */
public interface WorkflowEngineGetter {

    /**
     * 获取流程引擎实例
     *
     * @return 流程引擎实例
     */
    ProcessEngine getProcessEngine();

}