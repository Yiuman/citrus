package com.github.yiuman.citrus.support.exception;

import com.github.yiuman.citrus.support.http.ResponseStatusCode;

/**
 * 检验异常
 *
 * @author yiuman
 * @date 2020/4/6
 */
public class ValidateException extends RestException {

    public ValidateException(String message) {
        super(message, ResponseStatusCode.BAD_REQUEST);

    }

    public ValidateException(Integer code) {
        super(code);
    }

    public ValidateException(String message, Integer code) {
        super(message, code);
    }

    public ValidateException(String message, Throwable cause, Integer code) {
        super(message, cause, code);
    }

    public ValidateException(Throwable cause, Integer code) {
        super(cause, code);
    }

    protected ValidateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Integer code) {
        super(message, cause, enableSuppression, writableStackTrace, code);
    }
}
