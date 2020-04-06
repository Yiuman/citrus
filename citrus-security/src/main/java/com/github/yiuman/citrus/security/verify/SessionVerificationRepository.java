package com.github.yiuman.citrus.security.verify;

import com.github.yiuman.citrus.support.utils.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

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
    public void save(HttpServletRequest request, HttpServletResponse response, Verification<?> verification) {
        String randomVerifyId = UUID.randomUUID().toString();
        request.getSession().setAttribute(SESSION_VERIFICATION_PARAMETER + randomVerifyId, verification);
        response.addCookie(new Cookie(SESSION_VERIFICATION_PARAMETER, randomVerifyId));

    }

    @Override
    public Verification<?> find(HttpServletRequest request) {
        String verifyId  = WebUtils.getCookie(request, SESSION_VERIFICATION_PARAMETER);
        return (Verification<?>) request.getSession().getAttribute(SESSION_VERIFICATION_PARAMETER+verifyId);
    }

    @Override
    public void remove(HttpServletRequest request) {
        request.getSession().removeAttribute(SESSION_VERIFICATION_PARAMETER);
    }
}
