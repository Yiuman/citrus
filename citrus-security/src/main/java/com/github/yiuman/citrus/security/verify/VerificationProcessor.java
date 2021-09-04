package com.github.yiuman.citrus.security.verify;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 验证处理器
 *
 * @param <T> 验证码实例类型
 * @author yiuman
 * @date 2020/3/22
 */
public interface VerificationProcessor<T extends Verification<?>> {

    /**
     * 根据当前请求构造验证实体实例
     *
     * @param httpServletRequest 当前请求
     * @return 验证实例
     */
    T generate(HttpServletRequest httpServletRequest);

    /**
     * 发送验证类型
     *
     * @param httpServletRequest 当前请求
     * @param response           当前响应
     * @throws Exception 一般为IO异常
     */
    void send(HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception;

    /**
     * 校验验证类型
     *
     * @param request 当前请求
     * @throws VerificationException 验证异常
     */
    void validate(HttpServletRequest request) throws VerificationException;

    /**
     * 验证码类型
     *
     * @return 获取验证码类型
     */
    String verificationType();

}