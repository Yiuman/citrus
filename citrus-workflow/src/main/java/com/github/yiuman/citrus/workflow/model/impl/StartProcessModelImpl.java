package com.github.yiuman.citrus.workflow.model.impl;

import com.github.yiuman.citrus.workflow.model.StartProcessModel;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * 启动流程模型实现类
 *
 * @author yiuman
 * @date 2020/12/14
 */
@Data
@Builder
public class StartProcessModelImpl implements StartProcessModel {

    private String processDefineId;

    private String businessKey;

    private Map<String, Object> variables;

    private String userId;

    private Set<String> candidateOrAssigned;

    public StartProcessModelImpl() {
    }

    @Override
    public String getProcessDefineId() {
        return processDefineId;
    }

    @Override
    public String getBusinessKey() {
        return businessKey;
    }

    @Override
    public Map<String, Object> getVariables() {
        return variables;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public Set<String> getCandidateOrAssigned() {
        return candidateOrAssigned;
    }
}
