package com.github.yiuman.citrus.mda.exception;

/**
 * DML操作运行时异常
 *
 * @author yiuman
 * @date 2021/4/27
 */

public class DmlException extends RuntimeException {

    public DmlException() {
    }

    public DmlException(String message) {
        super(message);
    }

    public DmlException(String message, Throwable cause) {
        super(message, cause);
    }

    public DmlException(Throwable cause) {
        super(cause);
    }
}