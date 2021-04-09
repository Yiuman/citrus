package com.github.yiuman.citrus.workflow.service.impl;

import com.github.yiuman.citrus.support.utils.SpringUtils;
import com.github.yiuman.citrus.workflow.service.WorkflowEngineGetter;
import org.activiti.engine.ProcessEngine;

import java.util.Optional;

/**
 * 默认的流程引擎获取方式
 *
 * @author yiuman
 * @date 2020/12/17
 */
public class WorkflowEngineGetterImpl implements WorkflowEngineGetter {

    private ProcessEngine processEngine;

    public WorkflowEngineGetterImpl() {
    }

    @Override
    public ProcessEngine getProcessEngine() {
        return processEngine = Optional.ofNullable(this.processEngine)
                .orElse(SpringUtils.getBean(ProcessEngine.class, true));
    }
}
