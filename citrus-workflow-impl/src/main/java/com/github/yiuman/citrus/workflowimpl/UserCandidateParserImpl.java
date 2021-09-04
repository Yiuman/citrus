package com.github.yiuman.citrus.workflowimpl;

import com.github.yiuman.citrus.support.crud.query.builder.QueryBuilders;
import com.github.yiuman.citrus.system.dto.UserDto;
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
 * 用户候选人解析器
 *
 * @author yiuman
 * @date 2020/12/28
 */
@Component
public class UserCandidateParserImpl implements CandidateParser {

    private final UserService userService;

    public UserCandidateParserImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean support(String dimension) {
        return WorkflowDimension.USER.equals(dimension);
    }

    @Override
    public <T extends CandidateModel> List<String> parse(WorkflowContext workflowContext, T candidateModel) {
        QueryBuilders.<UserDto>lambda().in(UserDto::getUuid, candidateModel.getValues()).toQuery();
        List<UserDto> userDtos = userService.list();
        if (CollectionUtils.isEmpty(userDtos)) {
            return null;
        }

        return userDtos.stream().map(UserDto::getUserId).map(String::valueOf).collect(Collectors.toList());
    }
}
