package com.github.yiuman.citrus.workflow.resolver;

/**
 * 工作流候选人解析的维度
 *
 * @author yiuman
 * @date 2020/12/28
 */
public interface WorkflowDimension {

    /**
     * 用户
     */
    String USER = "user";

    /**
     * 角色
     */
    String ROLE = "role";

    /**
     * 部门
     */
    String DEPT = "dept";

    /**
     * 权限
     */
    String AUTH = "auth";
}