package com.github.yiuman.citrus.workflow.parser;

import com.github.yiuman.citrus.system.entity.User;
import com.github.yiuman.citrus.system.service.RbacMixinService;
import com.github.yiuman.citrus.workflow.model.CandidateModel;
import com.github.yiuman.citrus.workflow.model.WorkflowContext;
import com.github.yiuman.citrus.workflow.resolver.CandidateParser;
import com.github.yiuman.citrus.workflow.resolver.WorkflowDimension;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门候选人解析器，返回定义部门内的所有用户
 *
 * @author yiuman
 * @date 2020/12/28
 */
@Component
public class DeptCandidateParserImpl implements CandidateParser {

    private final RbacMixinService rbacMixinService;

    public DeptCandidateParserImpl(RbacMixinService rbacMixinService) {
        this.rbacMixinService = rbacMixinService;
    }

    @Override
    public boolean support(String dimension) {
        return WorkflowDimension.DEPT.equals(dimension);
    }

    @Override
    public <T extends CandidateModel> List<String> parse(WorkflowContext workflowContext, T candidateModel) {
        List<Long> deptIds = candidateModel.getValues().stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());

        List<User> usersByDeptIds = rbacMixinService.getUserService().getUsersByDeptIds(deptIds);
        if (CollectionUtils.isEmpty(usersByDeptIds)) {
            return null;
        }

        return usersByDeptIds.stream().map(User::getUserId).map(String::valueOf).collect(Collectors.toList());
    }
}
