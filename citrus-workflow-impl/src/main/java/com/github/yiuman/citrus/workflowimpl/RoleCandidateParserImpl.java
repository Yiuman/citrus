package com.github.yiuman.citrus.workflowimpl;

import com.github.yiuman.citrus.system.entity.User;
import com.github.yiuman.citrus.system.service.UserService;
import com.github.yiuman.citrus.workflow.model.CandidateModel;
import com.github.yiuman.citrus.workflow.model.WorkflowContext;
import com.github.yiuman.citrus.workflow.resolver.CandidateParser;
import com.github.yiuman.citrus.workflow.resolver.WorkflowDimension;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色候选人解析器
 *
 * @author yiuman
 * @date 2020/12/28
 */
@Component
public class RoleCandidateParserImpl implements CandidateParser {

    private final UserService userService;

    public RoleCandidateParserImpl(UserService userService) {

        this.userService = userService;
    }

    @Override
    public boolean support(String dimension) {
        return WorkflowDimension.ROLE.equals(dimension);
    }

    @Override
    public <T extends CandidateModel> List<String> parse(WorkflowContext workflowContext, T candidateModel) {
        List<String> roleIds = candidateModel.getValues();
        List<User> usersByRoleIds = userService.getUsersByRoleIds(
                roleIds.parallelStream().map(Long::valueOf).collect(Collectors.toList())
        );

        if (CollectionUtils.isEmpty(usersByRoleIds)) {
            return null;
        }

        return usersByRoleIds.stream().map(User::getUserId).map(String::valueOf).collect(Collectors.toList());


    }
}
