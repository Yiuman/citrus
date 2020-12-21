package com.github.yiuman.citrus.workflow.resolver;

import com.github.yiuman.citrus.workflow.model.CandidateModel;

import java.util.List;

/**
 * @author yiuman
 * @date 2020/12/18
 */
public interface CandidateParser {

    boolean support(String dimension);

    <T extends CandidateModel> List<String> parse(T candidateModel);
}