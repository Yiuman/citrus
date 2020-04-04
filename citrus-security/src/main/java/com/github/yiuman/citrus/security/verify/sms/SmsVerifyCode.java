package com.github.yiuman.citrus.security.verify.sms;

import com.github.yiuman.citrus.security.verify.AbstractStringVerification;

/**
 * 短信认证码
 *
 * @author yiuman
 * @date 2020/3/22
 */
public class SmsVerifyCode extends AbstractStringVerification {

    public SmsVerifyCode(String code) {
        super(code);
    }

    public SmsVerifyCode(String code, long expireTime) {
        super(code, expireTime);
    }
}
