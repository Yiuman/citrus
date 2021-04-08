package com.github.yiuman.citrus.workflow.model.impl;

import com.github.yiuman.citrus.workflow.model.CandidateModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 候选人模型的实现
 *
 * @author yiuman
 * @date 2020/12/18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateModelImpl implements CandidateModel {

    private String dimension;

    private List<String> values;

}
