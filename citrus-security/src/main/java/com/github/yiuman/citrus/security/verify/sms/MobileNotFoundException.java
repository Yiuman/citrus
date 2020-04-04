package com.github.yiuman.citrus.security.verify.sms;

import org.springframework.security.core.AuthenticationException;

/**
 * @author yiuman
 * @date 2020/3/22
 */
public class MobileNotFoundException extends AuthenticationException {

    public MobileNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public MobileNotFoundException(String msg) {
        super(msg);
    }
}
