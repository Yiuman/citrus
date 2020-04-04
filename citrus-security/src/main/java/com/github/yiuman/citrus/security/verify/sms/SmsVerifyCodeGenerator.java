package com.github.yiuman.citrus.security.verify.sms;


import com.github.yiuman.citrus.security.verify.VerificationGenerator;
import com.github.yiuman.citrus.security.verify.VerifyUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 短信验证
 *
 * @author yiuman
 * @date 2020/3/22
 */
public class SmsVerifyCodeGenerator implements VerificationGenerator<SmsVerifyCode> {

    public SmsVerifyCodeGenerator() {
    }

    @Override
    public SmsVerifyCode generate(HttpServletRequest httpServletRequest) {
        return new SmsVerifyCode(VerifyUtils.generateVerifyCode(6));
    }
}
