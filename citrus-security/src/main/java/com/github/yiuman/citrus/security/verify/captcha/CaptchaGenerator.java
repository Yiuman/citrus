package com.github.yiuman.citrus.security.verify.captcha;

import com.github.yiuman.citrus.security.verify.VerificationGenerator;
import com.github.yiuman.citrus.security.verify.VerifyProperties;
import com.github.yiuman.citrus.security.verify.VerifyUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 图片验证码构造器
 *
 * @author yiuman
 * @date 2020/3/22
 */
public class CaptchaGenerator implements VerificationGenerator<Captcha> {

    private final VerifyProperties verifyProperties;

    public CaptchaGenerator(VerifyProperties verifyProperties) {
        this.verifyProperties = verifyProperties;
    }

    @Override
    public Captcha generate(HttpServletRequest httpServletRequest) {
        String code = VerifyUtils.generateVerifyCode(verifyProperties.getVerifyCodeSize());
        return new Captcha(code, VerifyUtils.bufferedImage(verifyProperties.getCaptchaWidth(), verifyProperties.getCaptchaHeight(), code));
    }
}
