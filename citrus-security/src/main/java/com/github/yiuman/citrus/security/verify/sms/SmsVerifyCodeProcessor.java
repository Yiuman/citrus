package com.github.yiuman.citrus.security.verify.sms;

import com.github.yiuman.citrus.security.verify.AbstractStringVerificationProcessor;
import com.github.yiuman.citrus.security.verify.VerificationRepository;
import com.github.yiuman.citrus.security.verify.VerifyProperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yiuman
 * @date 2020/3/22
 */
public class SmsVerifyCodeProcessor extends AbstractStringVerificationProcessor<SmsVerifyCode> {

    public SmsVerifyCodeProcessor(VerificationRepository verificationRepository, VerifyProperties verifyProperties) {
        super(verificationRepository, verifyProperties);
    }

    @Override
    public SmsVerifyCode generate(HttpServletRequest httpServletRequest) {
        return new SmsVerifyCodeGenerator().generate(httpServletRequest);
    }

    @Override
    public void send(HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
        //Nothing to do
    }
}
