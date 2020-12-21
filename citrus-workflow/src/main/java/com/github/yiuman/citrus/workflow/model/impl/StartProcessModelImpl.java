package com.github.yiuman.citrus.workflow.model.impl;

import com.github.yiuman.citrus.workflow.model.StartProcessModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 启动流程模型实现类
 *
 * @author yiuman
 * @date 2020/12/14
 */
@Data
@Builder
public class StartProcessModelImpl implements StartProcessModel {

    private String processDefineKey;

    private String businessKey;

    private Map<String, Object> variables;

    private String userId;

    private List<String> candidateOrAssigned;

    @Override
    public String getProcessDefineKey() {
        return processDefineKey;
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
    public List<String> getCandidateOrAssigned() {
        return candidateOrAssigned;
    }
}
