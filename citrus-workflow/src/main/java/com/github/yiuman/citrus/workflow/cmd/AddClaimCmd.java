package com.github.yiuman.citrus.workflow.cmd;

import com.github.yiuman.citrus.workflow.exception.WorkflowException;
import lombok.Builder;
import org.activiti.bpmn.model.Activity;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.MultiInstanceLoopCharacteristics;
import org.activiti.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.activiti.engine.impl.history.HistoryManager;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntityManager;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;

/**
 * 加签命令，用于会签中的加签
 *
 * @author yiuman
 * @date 2021/4/2
 */
@Builder
public class AddClaimCmd implements Command<Void> {

    /**
     * 参数名：当前的任务实例
     */
    protected final String NUMBER_OF_INSTANCES = "nrOfInstances";

    /**
     * 参数名：执行中的任务实例
     */
    protected final String NUMBER_OF_ACTIVE_INSTANCES = "nrOfActiveInstances";
    /**
     * 任务ID，需要加签的任务
     */
    private String taskId;

    /**
     * 处理人
     */
    private String assignee;

    public AddClaimCmd() {
    }

    @Override
    public Void execute(CommandContext commandContext) {
        TaskEntityManager taskEntityManager = commandContext.getTaskEntityManager();
        ExecutionEntityManager executionEntityManager = commandContext.getExecutionEntityManager();

        //1.根据任务id获取任务实例
        TaskEntity taskEntity = taskEntityManager.findById(taskId);
        //2.根据当前任务获取当前的执行实例
        ExecutionEntity multiExecutionEntity = executionEntityManager.findById(taskEntity.getExecutionId());

        BpmnModel bpmnModel = ProcessDefinitionUtil.getBpmnModel(multiExecutionEntity.getProcessDefinitionId());
        Activity activityElement = (Activity) bpmnModel.getFlowElement(multiExecutionEntity.getCurrentActivityId());
        MultiInstanceLoopCharacteristics loopCharacteristics = activityElement.getLoopCharacteristics();
        //3.判断当前执行的节点是否为会签节点
        if (loopCharacteristics == null) {
            throw new WorkflowException(String.format("This FlowElement is not a multi-instance node:%s", activityElement.getId()));
        }
        //4.判断是否是并行多实例
        if (loopCharacteristics.isSequential()) {
            throw new WorkflowException(String.format("This FlowElement  is not a parallel node:%s", activityElement.getId()));
        }

        //5.获取父执行实例（即流程的执行实例）
        ExecutionEntity parentExecutionEntity = multiExecutionEntity.getParent();
        //6.创建一个新的子执行实例,设置处理人
        ExecutionEntity newChildExecution = executionEntityManager.createChildExecution(parentExecutionEntity);
        newChildExecution.setCurrentFlowElement(activityElement);
        newChildExecution.setVariableLocal("assignee", assignee);

        //7.当前的任务实例与当前执行中的实例都+1  因为已经加签
        Integer nrOfInstances = (Integer) parentExecutionEntity.getVariableLocal(NUMBER_OF_INSTANCES);
        Integer nrOfActiveInstances = (Integer) parentExecutionEntity.getVariableLocal(NUMBER_OF_ACTIVE_INSTANCES);
        parentExecutionEntity.setVariableLocal(NUMBER_OF_INSTANCES, nrOfInstances + 1);
        parentExecutionEntity.setVariableLocal(NUMBER_OF_ACTIVE_INSTANCES, nrOfActiveInstances + 1);
        //8.通知活动开始
        HistoryManager historyManager = commandContext.getHistoryManager();
        historyManager.recordActivityStart(newChildExecution);
        //9.获取处理行为类并执行行为
        ParallelMultiInstanceBehavior parallelMultiInstanceBehavior = (ParallelMultiInstanceBehavior) activityElement.getBehavior();
        AbstractBpmnActivityBehavior innerActivityBehavior = parallelMultiInstanceBehavior.getInnerActivityBehavior();
        innerActivityBehavior.execute(newChildExecution);
        return null;
    }
}
