package com.github.yiuman.citrus.security.verify;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
    public static final String SESSION_VERIFICATION_PARAMETER = "SESSION_VERIFY_ID";

    public SessionVerificationRepository() {
    }

    @Override
    public void save(HttpServletRequest request, HttpServletResponse response, Verification<?> verification) {
        HttpSession session = request.getSession();
        session.setAttribute(SESSION_VERIFICATION_PARAMETER, verification);
    }

    @Override
    public Verification<?> find(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (Verification<?>) session.getAttribute(SESSION_VERIFICATION_PARAMETER);
    }

    @Override
    public void remove(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(SESSION_VERIFICATION_PARAMETER);
    }
}
