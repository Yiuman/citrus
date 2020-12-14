package com.github.yiuman.citrus.workflow.service;

import org.activiti.engine.*;

import javax.annotation.Resource;

/**
 * Activiti相关服务类的集成
 *
 * @author yiuman
 * @date 2020/12/11
 */
public abstract class BaseFlowableService {

    @Resource
    protected ProcessEngine processEngine;

    @Resource
    protected RepositoryService repositoryService;

    @Resource
    protected RuntimeService runtimeService;

    @Resource
    protected TaskService taskService;

    @Resource
    protected HistoryService historyService;

    @Resource
    protected ManagementService managementService;

}
