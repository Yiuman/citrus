package com.github.yiuman.citrus.workflowimpl;

import com.github.yiuman.citrus.system.entity.RoleAuthority;
import com.github.yiuman.citrus.system.service.RbacMixinService;
import com.github.yiuman.citrus.workflow.model.CandidateModel;
import com.github.yiuman.citrus.workflow.model.WorkflowContext;
import com.github.yiuman.citrus.workflow.model.impl.CandidateModelImpl;
import com.github.yiuman.citrus.workflow.resolver.CandidateParser;
import com.github.yiuman.citrus.workflow.resolver.WorkflowDimension;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限候选人解析器，返回用于权限集合的用户
 *
 * @author yiuman
 * @date 2020/12/28
 */
@Component
public class AuthCandidateParserImpl implements CandidateParser {

    private final RbacMixinService rbacMixinService;

    private final RoleCandidateParserImpl roleCandidateParser;

    public AuthCandidateParserImpl(RbacMixinService rbacMixinService, RoleCandidateParserImpl roleCandidateParser) {
        this.rbacMixinService = rbacMixinService;
        this.roleCandidateParser = roleCandidateParser;
    }

    @Override
    public boolean support(String dimension) {
        return WorkflowDimension.AUTH.equals(dimension);
    }

    @Override
    public <T extends CandidateModel> List<String> parse(WorkflowContext workflowContext, T candidateModel) {
        List<Long> authIds = candidateModel.getValues().stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());

        List<RoleAuthority> roleAuthorityByAuthAuthIds = rbacMixinService
                .getRoleService().getRoleAuthorityByAuthIds(authIds);

        List<String> roleIds = roleAuthorityByAuthAuthIds.stream()
                .map(RoleAuthority::getRoleId)
                .map(String::valueOf)
                .collect(Collectors.toList());
        return roleCandidateParser.parse(workflowContext, CandidateModelImpl.builder().dimension(WorkflowDimension.ROLE).values(roleIds).build());
    }
}
