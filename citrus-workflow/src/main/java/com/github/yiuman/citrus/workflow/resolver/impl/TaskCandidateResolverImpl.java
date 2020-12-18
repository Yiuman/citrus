package com.github.yiuman.citrus.workflow.resolver.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yiuman.citrus.workflow.model.impl.CandidateModelImpl;
import com.github.yiuman.citrus.workflow.resolver.CandidateParser;
import com.github.yiuman.citrus.workflow.resolver.TaskCandidateResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    private final Set<CandidateParser> candidateParsers;

    public TaskCandidateResolverImpl(ObjectMapper objectMapper, Set<CandidateParser> candidateParsers) {
        this.objectMapper = objectMapper;
        this.candidateParsers = candidateParsers;
    }

    @Override
    public List<String> resolve(List<String> taskCandidateDefine) {
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

                        //找到就进行解释
                        if(candidateParser.isPresent()){
                            candidateParser.get().parse(candidateModel.getDimensionValue());
                        }else{
                            realUserIds.add(candidateModel.getDimensionValue());
                        }

                    } catch (JsonProcessingException ignore) {
                        realUserIds.add(taskCandidate);
                    }
                });
        return realUserIds;
    }
}
