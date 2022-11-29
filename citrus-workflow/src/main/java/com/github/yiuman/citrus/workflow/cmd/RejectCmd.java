package com.github.yiuman.citrus.workflow.cmd;

import cn.hutool.core.collection.CollUtil;
import com.github.yiuman.citrus.workflow.exception.WorkflowException;
import com.github.yiuman.citrus.workflow.utils.ProcessUtils;
import lombok.Builder;
import org.activiti.bpmn.model.Activity;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

import java.util.List;

/**
 * 驳回命令
 *
 * @author yiuman
 * @date 2022/11/21
 */
@Builder
public class RejectCmd implements Command<Void> {

    private final String executionId;

    @Builder.Default
    private String reason = "reject";

    public RejectCmd(String executionId) {
        this.executionId = executionId;
    }

    public RejectCmd(String executionId, String reason) {
        this.executionId = executionId;
        this.reason = reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public Void execute(CommandContext commandContext) {
        //1.找到当前执行实例
        ExecutionEntity executionEntity = commandContext.getExecutionEntityManager().findById(executionId);
        List<Activity> activities = ProcessUtils.findPreActivities(executionEntity);
        if (CollUtil.isEmpty(activities)) {
            throw new WorkflowException("未找到当前活动的前置活动节点");
        }

        if (activities.size() > 1) {
            throw new WorkflowException("前置活动节点大于1，请选择进行驳回");
        }

        Activity activity = activities.get(0);
        new JumpTaskCmd(executionEntity.getId(), activity.getId()).execute(commandContext);
        return null;

    }

}
