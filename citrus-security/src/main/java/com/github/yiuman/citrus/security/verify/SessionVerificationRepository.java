package com.github.yiuman.citrus.security.verify;

import javax.servlet.http.HttpServletRequest;

/**
 * Session的验证信息存储仓库
 *
 * @author yiuman
 * @date 2020/3/23
 */
public class SessionVerificationRepository implements VerificationRepository {

    /**
     * session中key的前缀
     */
    public final static String SESSION_VERIFICATION_PARAMETER = "SESSION_VERIFY_ID";

    public SessionVerificationRepository() {
    }

    @Override
    public void save(HttpServletRequest httpServletRequest, Verification<?> verification) {
        httpServletRequest.getSession().setAttribute(SESSION_VERIFICATION_PARAMETER, verification);
    }

    @Override
    public Verification<?> find(HttpServletRequest request) {
        return (Verification<?>) request.getSession().getAttribute(SESSION_VERIFICATION_PARAMETER);
    }

    @Override
    public void remove(HttpServletRequest request) {
        request.getSession().removeAttribute(SESSION_VERIFICATION_PARAMETER);
    }
}
