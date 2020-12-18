package com.github.yiuman.citrus.workflow.exception;

/**
 * 工作流相关的异常
 *
 * @author yiuman
 * @date 2020/12/18
 */
public class WorkflowException extends RuntimeException {

    public WorkflowException(String message) {
        super(message);
    }

    public WorkflowException(String message, Throwable cause) {
        super(message, cause);
    }
}