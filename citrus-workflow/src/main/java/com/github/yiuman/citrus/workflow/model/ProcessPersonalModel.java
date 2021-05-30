package com.github.yiuman.citrus.workflow.model;

import java.util.List;

/**
 * 流程人员模型，用于定义流程环节中的操作的当前处理人与下环节候选人或办理人
 *
 * @author yiuman
 * @date 2020/12/14
 */
public interface ProcessPersonalModel {

    /**
     * 当前的处理人ID
     *
     * @return 处理人主键标识
     */
    String getUserId();

    /**
     * 1.下一个环节的候选人ID，若是只有一个，则为处理人
     *
     * @return 候选人ID列表
     * @see CandidateModel 2.也可能为维度候选人模型的JSON字符串，
     */
    List<String> getCandidateOrAssigned();
}