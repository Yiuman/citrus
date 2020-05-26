package com.github.yiuman.citrus.security.verify;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 验证信息仓库
 *
 * @author yiuman
 * @date 2020/3/22
 */
public interface VerificationRepository {

    /**
     * 保存到仓库
     *
     * @param request      当前请求
     * @param response     当前响应
     * @param verification 验证信息
     */
    void save(HttpServletRequest request, HttpServletResponse response, Verification<?> verification);

    /**
     * 根据当前请求获取验证信息
     *
     * @param request 当前请求
     * @return 验证信息
     */
    Verification<?> find(HttpServletRequest request);


    /**
     * 移除当前请求相关会话的验证贮存的信息
     *
     * @param request 当前请求
     */
    void remove(HttpServletRequest request);

}