package com.github.yiuman.citrus.security.verify;

import javax.servlet.http.HttpServletRequest;

/**
 * 验证码构造器
 *
 * @author yiuman
 * @date 2020/3/22
 */
public interface VerificationGenerator<T extends Verification<?>> {

    T generate(HttpServletRequest httpServletRequest);

}