package com.github.yiuman.citrus.security.verify;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yiuman
 * @date 2020/4/6
 */
public class JwtVerificationRepository implements VerificationRepository {


    @Override
    public void save(HttpServletRequest request, HttpServletResponse response, Verification<?> verification) {

    }

    @Override
    public Verification<?> find(HttpServletRequest request) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public void remove(HttpServletRequest request) {
        throw new UnsupportedOperationException("Method not implemented.");
    }
}
