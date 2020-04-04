package com.github.yiuman.citrus.security.verify.captcha;

import com.github.yiuman.citrus.security.verify.AbstractStringVerificationProcessor;
import com.github.yiuman.citrus.security.verify.VerificationRepository;
import com.github.yiuman.citrus.security.verify.VerifyProperties;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码处理器，实现发送，检验逻辑
 *
 * @author yiuman
 * @date 2020/3/22
 */
public class CaptchaProcessor extends AbstractStringVerificationProcessor<Captcha> {

    public CaptchaProcessor(VerificationRepository verificationRepository, VerifyProperties verifyProperties) {
        super(verificationRepository, verifyProperties);
    }

    @Override
    public Captcha generate(HttpServletRequest httpServletRequest) {
       return new CaptchaGenerator(verifyProperties).generate(httpServletRequest);
    }

    @Override
    public void send(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        Captcha captcha = generate(httpServletRequest);
        verificationRepository.save(httpServletRequest, captcha);
        ImageIO.write(captcha.getImage(), "JPEG", httpServletResponse.getOutputStream());
    }
}
