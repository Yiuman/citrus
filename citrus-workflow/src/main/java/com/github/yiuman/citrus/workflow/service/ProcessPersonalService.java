package com.github.yiuman.citrus.workflow.service;

import com.github.yiuman.citrus.workflow.model.ProcessPersonalModel;

/**
 * 流程人员相关服务接口
 *
 * @author yiuman
 * @date 2020/12/18
 */
public interface ProcessPersonalService {

    /**
     * 获取流程的人员信息模型
     *
     * @return 流程人员信息模型
     */
    ProcessPersonalModel getModel();
}