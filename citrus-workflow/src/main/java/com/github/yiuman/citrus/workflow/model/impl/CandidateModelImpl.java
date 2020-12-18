package com.github.yiuman.citrus.workflow.model.impl;

import com.github.yiuman.citrus.workflow.model.CandidateModel;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * 候选人模型的实现
 *
 * @author yiuman
 * @date 2020/12/18
 */
@Data
@Builder
public class CandidateModelImpl implements CandidateModel {

    private String dimension;

    private String dimensionValue;

}
