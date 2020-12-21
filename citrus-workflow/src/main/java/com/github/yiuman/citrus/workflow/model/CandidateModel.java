package com.github.yiuman.citrus.workflow.model;

import java.util.List;

/**
 * 候选人模型
 *
 * @author yiuman
 * @date 2020/12/18
 */
public interface CandidateModel {

    /**
     * 维度，用于区分候选人的维度<br/>
     * 如，维度为用户、部门、角色
     *
     * @return 实体类型的类名
     */
    String getDimension();

    /**
     * 维度的值<br/>
     * 如用户则为用户ID，如角色则为角色ID
     *
     * @return 用户ID集合
     */
    List<String> getValues();

}