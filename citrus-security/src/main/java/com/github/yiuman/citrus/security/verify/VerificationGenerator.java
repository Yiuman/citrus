package com.github.yiuman.citrus.security.verify;

import javax.servlet.http.HttpServletRequest;

/**
 * 验证码构造器
 *
 * @author yiuman
 * @date 2020/3/22
 */
public interface VerificationGenerator<T extends Verification<?>> {

    /**
     * 根据当前请求构造验证信息
     *
     * @param httpServletRequest 当前请求
     * @return 验证信息实体 如验证码
     */
    T generate(HttpServletRequest httpServletRequest);

}