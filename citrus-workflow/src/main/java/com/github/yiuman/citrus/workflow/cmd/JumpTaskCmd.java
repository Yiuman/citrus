package com.github.yiuman.citrus.workflow.cmd;

import com.github.yiuman.citrus.workflow.exception.WorkflowException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.ActivitiEngineAgenda;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;

import java.util.Objects;

/**
 * 任务跳转命令
 *
 * @author yiuman
 * @date 2021/3/30
 */
@Builder
@AllArgsConstructor
public class JumpTaskCmd implements Command<Void> {

    /**
     * 当前的执行ID
     */
    private String executionId;

    /**
     * 目标任务定义的key
     */
    private String targetTaskKey;

    public JumpTaskCmd() {
    }

    @Override
    public Void execute(CommandContext commandContext) {
        //1.删除当前执行中的任务
        commandContext.getTaskEntityManager().findTasksByExecutionId(executionId)
                .forEach(task -> commandContext.getTaskEntityManager().deleteTask(task, "jump", false, false));
        //2.找到当前执行实例
        ExecutionEntity executionEntity = commandContext.getExecutionEntityManager().findById(executionId);
        //3.找到当前的流程
        Process process = ProcessDefinitionUtil.getProcess(executionEntity.getProcessDefinitionId());
        FlowElement targetFlowElement = process.getFlowElement(targetTaskKey);
        if (Objects.isNull(targetFlowElement)) {
            throw new WorkflowException(String.format("can not found FlowElement with key `%s`", targetTaskKey));
        }
        //4.将目标节点设置到当前的执行实例中
        executionEntity.setCurrentFlowElement(targetFlowElement);

        //5.触发实例流转
        ActivitiEngineAgenda agenda = commandContext.getAgenda();
        agenda.planContinueProcessInCompensation(executionEntity);

        return null;
    }
}
