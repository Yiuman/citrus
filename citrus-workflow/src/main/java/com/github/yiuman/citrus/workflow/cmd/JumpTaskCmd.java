package com.github.yiuman.citrus.workflow.cmd;

import com.github.yiuman.citrus.workflow.exception.WorkflowException;
import lombok.Builder;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.ActivitiEngineAgenda;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.deploy.DeploymentManager;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.repository.ProcessDefinition;

import java.util.Objects;

/**
 * 任务跳转命令
 *
 * @author yiuman
 * @date 2021/3/30
 */
@Builder
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
        DeploymentManager deploymentManager = commandContext.getProcessEngineConfiguration().getDeploymentManager();
        ProcessDefinition processDefinition = deploymentManager.findDeployedProcessDefinitionById(executionEntity.getProcessDefinitionId());
        //3.找到当前的流程
        Process process = deploymentManager.resolveProcessDefinition(processDefinition).getProcess();
        FlowElement flowElement = process.getFlowElement(targetTaskKey);
        if (Objects.isNull(flowElement)) {
            throw new WorkflowException(String.format("can not found FlowElement with key `%s`", targetTaskKey));
        }
        //4.将目标节点设置到当前的执行实例中
        executionEntity.setCurrentFlowElement(flowElement);
        ActivitiEngineAgenda agenda = commandContext.getAgenda();
        //5.触发实例流转
        agenda.planContinueProcessInCompensation(executionEntity);

        return null;
    }
}
