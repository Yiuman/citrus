package com.github.yiuman.citrus.workflow.resolver;

import com.github.yiuman.citrus.workflow.model.WorkflowContext;

import java.util.List;

/**
 * 任务的候选人解析器<br/>
 * 根据任务定义的候选人的字符串解析成相关候选人用户ID
 *
 * @author yiuman
 * @date 2020/12/18
 */
@FunctionalInterface
public interface TaskCandidateResolver {

    List<String> resolve(WorkflowContext workflowContext, List<String> taskCandidateDefine);
}