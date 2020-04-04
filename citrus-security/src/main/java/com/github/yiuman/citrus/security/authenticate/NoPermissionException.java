package com.github.yiuman.citrus.security.authenticate;

import org.springframework.security.core.AuthenticationException;

/**
 * 没权限异常
 * @author yiuman
 * @date 2020/4/4
 */
public class NoPermissionException extends AuthenticationException {

    private final static String  DEFAULT_MESSAGE= "No permission";

    public NoPermissionException(String msg, Throwable t) {
        super(msg, t);
    }

    public NoPermissionException(String msg) {
        super(msg);
    }

    public NoPermissionException(){
        super(DEFAULT_MESSAGE);
    }
}
