package com.github.yiuman.citrus.workflow.model;

import java.util.Map;

/**
 * 启动流程的模型
 *
 * @author yiuman
 * @date 2020/12/14
 */
public interface StartProcessModel extends ProcessPersonalModel {

    /**
     * 获取流程定义Key，用于启动流程或处理流程相关操作
     *
     * @return 流程定义ID
     */
    String getProcessDefineKey();


    /**
     * 获取业务主键
     *
     * @return 关联的业务的主键标识
     */
    String getBusinessKey();


    /**
     * 流程变量
     *
     * @return 获取流程变量
     */
    Map<String, Object> getVariables();

}