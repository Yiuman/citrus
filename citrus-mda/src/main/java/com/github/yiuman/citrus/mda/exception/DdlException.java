package com.github.yiuman.citrus.mda.exception;

/**
 * DDL操作运行时异常
 *
 * @author yiuman
 * @date 2021/4/27
 */
public class DdlException extends RuntimeException {

    public DdlException() {
    }

    public DdlException(String message) {
        super(message);
    }

    public DdlException(String message, Throwable cause) {
        super(message, cause);
    }

    public DdlException(Throwable cause) {
        super(cause);
    }
}