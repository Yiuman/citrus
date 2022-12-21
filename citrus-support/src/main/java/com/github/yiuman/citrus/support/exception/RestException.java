package com.github.yiuman.citrus.support.exception;

/**
 * Rest异常
 *
 * @author yiuman
 * @date 2020/4/6
 */
public class RestException extends RuntimeException {

    private final Integer code;

    public RestException(Integer code) {
        this.code = code;
    }

    public RestException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public RestException(String message, Throwable cause, Integer code) {
        super(message, cause);
        this.code = code;
    }

    public RestException(Throwable cause, Integer code) {
        super(cause);
        this.code = code;
    }

    protected RestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Integer code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
