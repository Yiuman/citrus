package com.github.yiuman.citrus.workflow.resolver.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import com.github.yiuman.citrus.support.utils.SpringUtils;
import com.github.yiuman.citrus.workflow.model.impl.WorkflowContextImpl;
import com.github.yiuman.citrus.workflow.resolver.ExpressionResolver;
import com.github.yiuman.citrus.workflow.resolver.TaskCandidateResolver;
import com.github.yiuman.citrus.workflow.service.impl.WorkflowServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 处理人表达式解析器
 *
 * @author yiuman
 * @date 2021/4/8
 */
@Component
@Slf4j
public class Assignments implements ExpressionResolver<List<String>> {

    private final TaskCandidateResolver taskCandidateResolver;

    private final ObjectMapper objectMapper;

    public Assignments(TaskCandidateResolver taskCandidateResolver, ObjectMapper objectMapper) {
        this.taskCandidateResolver = taskCandidateResolver;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<String> resolve(DelegateExecution execution, String expressionStr) throws Exception {
        WorkflowServiceImpl workflowService = SpringUtils.getBean(WorkflowServiceImpl.class, true);
        ProcessEngine processEngine = workflowService.getProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //找到当前用于的ID
        String currentUserId = "anonymousUser";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication)) {
            currentUserId = (String) authentication.getPrincipal();
        }
        //构建流程上下文
        WorkflowContextImpl workflowContext = WorkflowContextImpl.builder()
                .processEngine(workflowService.getProcessEngine())
                .processInstance(
                        runtimeService
                                .createProcessInstanceQuery()
                                .processInstanceId(execution.getProcessInstanceId())
                                .singleResult()
                )
                .executionId(execution.getId())
                .flowElement(execution.getCurrentFlowElement())
                .currentUserId(currentUserId)
                .build();
        final List<String> stringArrayList = new ArrayList<>();
        try {
            List<?> list = objectMapper.readValue(expressionStr, List.class);
            list.forEach(LambdaUtils.consumerWrapper(item -> stringArrayList.add(objectMapper.writeValueAsString(item))));
        } catch (Exception ex) {
            stringArrayList.add(expressionStr);
            log.info("Assignments resolver exception: ", ex);
        }

        return taskCandidateResolver.resolve(workflowContext, stringArrayList);

    }
}
