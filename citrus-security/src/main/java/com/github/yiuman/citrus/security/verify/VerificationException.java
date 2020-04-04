package com.github.yiuman.citrus.security.verify;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证异常
 *
 * @author yiuman
 * @date 2020/3/22
 */
public class VerificationException extends AuthenticationException {

    public VerificationException(String msg, Throwable t) {
        super(msg, t);
    }

    public VerificationException(String msg) {
        super(msg);
    }
}
