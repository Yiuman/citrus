package com.github.yiuman.citrus.workflow.model.impl;

import com.github.yiuman.citrus.workflow.model.WorkflowContext;
import lombok.Builder;
import lombok.Data;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

/**
 * 流程上下文实现
 *
 * @author yiuman
 * @date 2020/12/29
 */
@Builder
@Data
public class WorkflowContextImpl implements WorkflowContext {

    private ProcessEngine processEngine;

    private ProcessInstance processInstance;

    private String executionId;

    private Task task;

    private String currentUserId;

    private FlowElement flowElement;


}
