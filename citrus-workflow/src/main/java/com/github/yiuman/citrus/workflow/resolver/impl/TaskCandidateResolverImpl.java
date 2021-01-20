package com.github.yiuman.citrus.workflow.resolver.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yiuman.citrus.workflow.model.WorkflowContext;
import com.github.yiuman.citrus.workflow.model.impl.CandidateModelImpl;
import com.github.yiuman.citrus.workflow.resolver.CandidateParser;
import com.github.yiuman.citrus.workflow.resolver.TaskCandidateResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 任务候选人解析器实现<br/>
 * 1.将候选人的字符串JSON转成候选人实体模型CandidateModel
 * 2.找到支持的维度翻译器，CandidateModel.getDimension()
 * 3.翻译
 *
 * @author yiuman
 * @date 2020/12/18
 */
@Component
public class TaskCandidateResolverImpl implements TaskCandidateResolver {

    private final ObjectMapper objectMapper;

    private final List<CandidateParser> candidateParsers;

    @Autowired(required = false)
    public TaskCandidateResolverImpl(@NonNull ObjectMapper objectMapper, @Nullable List<CandidateParser> candidateParsers) {
        this.objectMapper = objectMapper;
        this.candidateParsers = candidateParsers;
    }

    @Override
    public List<String> resolve(WorkflowContext workflowContext, List<String> taskCandidateDefine) {
        List<String> realUserIds = new ArrayList<>();
        taskCandidateDefine.parallelStream()
                .forEach(taskCandidate -> {
                    try {
                        //转化成候选人模型
                        CandidateModelImpl candidateModel = objectMapper
                                .readValue(taskCandidate, CandidateModelImpl.class);

                        //找到支持的解析器
                        Optional<CandidateParser> candidateParser = candidateParsers.parallelStream()
                                .filter(parser -> parser.support(candidateModel.getDimension()))
                                .findFirst();

                        //找到就进行解释,没找到直接加入
                        realUserIds.addAll(candidateParser.isPresent()
                                ? candidateParser.get().parse(workflowContext, candidateModel)
                                : candidateModel
                                .getValues()
                                .stream()
                                .map(dimensionValue -> String.format("%s#%s", candidateModel.getDimension(), dimensionValue))
                                .collect(Collectors.toList()));

                    } catch (JsonProcessingException ignore) {
                        realUserIds.add(taskCandidate);
                    }
                });
        return realUserIds;
    }
}
