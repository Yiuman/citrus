package com.github.yiuman.citrus.security.verify.captcha;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yiuman.citrus.security.verify.VerificationRepository;
import com.github.yiuman.citrus.security.verify.VerifyProperties;
import com.github.yiuman.citrus.support.http.ResponseEntity;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * @author yiuman
 * @date 2020/4/13
 */
public class Base64CaptchaProcessor extends CaptchaProcessor {

    private ObjectMapper objectMapper = new ObjectMapper();

    public Base64CaptchaProcessor(VerificationRepository verificationRepository, VerifyProperties verifyProperties) {
        super(verificationRepository, verifyProperties);
    }

    @Override
    public void send(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        Captcha captcha = generate(httpServletRequest);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(captcha.getImage(), "png", byteArrayOutputStream);
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(ResponseEntity.ok(Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray()))));
    }

    @Override
    public String verificationType() {
        return "base64captcha";
    }
}
