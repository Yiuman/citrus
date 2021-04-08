package com.github.yiuman.citrus.workflow.resolver;

import org.activiti.engine.delegate.DelegateExecution;

/**
 * 表达式解析器
 * 用于解析流程定义xml中，如${xxxResolver.resolve(execution,'[1,2,3,4]')}
 * <p>
 * On top of all process variables, there are a few default objects available to be used in expressions:
 * <p>
 * execution: The DelegateExecution that holds additional information about the ongoing execution.
 * <p>
 * task: The DelegateTask that holds additional information about the current Task. Note: Only works in expressions evaluated from task listeners.
 * <p>
 * authenticatedUserId: The id of the user that is currently authenticated. If no user is authenticated, the variable is not available.
 *
 * @author yiuman
 * @date 2021/4/8
 */
public interface ExpressionResolver<T> {

    /**
     * 用于解析当前执行对象以及定义的字符串，返回想要的对象
     *
     * @param execution     execution: The DelegateExecution that holds additional information about the ongoing execution. 执行时会从流程上下文传进来
     * @param expressionStr xml中一定的字符串
     * @return 解释后的模型
     * @throws Exception 解析异常
     */
    T resolve(DelegateExecution execution, String expressionStr) throws Exception;
}