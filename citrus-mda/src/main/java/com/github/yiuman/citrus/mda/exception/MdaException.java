package com.github.yiuman.citrus.mda.exception;

/**
 * @author yiuman
 * @date 2021/5/5
 */
public class MdaException extends RuntimeException {

    public MdaException() {
    }

    public MdaException(String message) {
        super(message);
    }

    public MdaException(String message, Throwable cause) {
        super(message, cause);
    }

    public MdaException(Throwable cause) {
        super(cause);
    }
}