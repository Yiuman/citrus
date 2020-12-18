package com.github.yiuman.citrus.workflow.model.impl;

import com.github.yiuman.citrus.workflow.model.TaskCompleteModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 任务完成模型实现类
 *
 * @author yiuman
 * @date 2020/12/14
 */
@Data
@Builder
public class TaskCompleteModelImpl implements TaskCompleteModel {

    private String taskId;

    private Map<String, Object> variables;

    private Map<String, Object> taskVariables;

    private String userId;

    private List<String> candidateOrAssigned;

    @Override
    public String getTaskId() {
        return taskId;
    }

    @Override
    public Map<String, Object> getVariables() {
        return variables;
    }

    @Override
    public Map<String, Object> getTaskVariables() {
        return taskVariables;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public List<String> getCandidateOrAssigned() {
        return candidateOrAssigned;
    }
}
