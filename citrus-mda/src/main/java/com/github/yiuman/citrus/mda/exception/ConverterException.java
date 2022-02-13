package com.github.yiuman.citrus.mda.exception;

/**
 * @author yiuman
 * @date 2021/4/29
 */

public class ConverterException extends RuntimeException {

    public ConverterException() {
    }

    public ConverterException(String message) {
        super(message);
    }

    public ConverterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConverterException(Throwable cause) {
        super(cause);
    }
}