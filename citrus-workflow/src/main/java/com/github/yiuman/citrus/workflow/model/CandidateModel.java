package com.github.yiuman.citrus.workflow.model;

/**
 * 候选人模型
 *
 * @author yiuman
 * @date 2020/12/18
 */
public interface CandidateModel {

    /**
     * 获取实体类型字符串
     *
     * @return 实体类型的类名
     */
    String getDimension();

    /**
     * 获取用户ID集合
     *
     * @return 用户ID集合
     */
    String getDimensionValue();

}