package com.github.yiuman.citrus.security.verify;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 验证处理器
 *
 * @author yiuman
 * @date 2020/3/22
 */
public interface VerificationProcessor<T extends Verification<?>> {

    T generate(HttpServletRequest httpServletRequest);

    /**
     * 发送验证类型
     *
     */
    void send(HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception;

    /**
     * 校验验证类型
     *
     * @param request 当前请求
     */
    void validate(HttpServletRequest request) throws VerificationException;

    /**
     * 验证码类型
     */
    String verificationType();

}