package com.github.yiuman.citrus.workflow.resolver;

import java.util.List;

/**
 * @author yiuman
 * @date 2020/12/18
 */
public interface CandidateParser {

    boolean support(String dimension);

    List<String> parse(String values);
}