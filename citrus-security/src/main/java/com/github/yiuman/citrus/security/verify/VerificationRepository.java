package com.github.yiuman.citrus.security.verify;

import javax.servlet.http.HttpServletRequest;

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
     * @param verification 验证信息
     */
    void save(HttpServletRequest httpServletRequest,Verification<?> verification);

    /**
     * 根据当前请求获取验证信息
     *
     * @param request 当前请求
     * @return 验证信息
     */
    Verification<?> find(HttpServletRequest request);


    void remove(HttpServletRequest request);

}