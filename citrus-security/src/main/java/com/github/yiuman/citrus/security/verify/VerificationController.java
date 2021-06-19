package com.github.yiuman.citrus.security.verify;

import com.github.yiuman.citrus.security.properties.CitrusProperties;
import com.github.yiuman.citrus.security.verify.captcha.Captcha;
import com.github.yiuman.citrus.security.verify.captcha.CaptchaGenerator;
import com.github.yiuman.citrus.support.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

/**
 * 验证信息控制器
 *
 * @author yiuman
 * @date 2020/3/30
 */
@SuppressWarnings("MVCPathVariableInspection")
@Controller
@RequestMapping("#{citrusProperties.security.verifyEndpointPrefix}")
public class VerificationController {

    private final CitrusProperties citrusProperties;

    private final List<? extends VerificationProcessor<?>> verificationProcessors;

    public VerificationController(CitrusProperties citrusProperties, List<? extends VerificationProcessor<?>> verificationProcessors) {
        this.citrusProperties = citrusProperties;
        this.verificationProcessors = verificationProcessors;
    }

    @GetMapping("/base64image")
    @ResponseBody
    public ResponseEntity<String> base64Image(HttpServletRequest request) throws IOException {
        CaptchaGenerator captchaGenerator = new CaptchaGenerator(citrusProperties.getVerify());
        Captcha captcha = captchaGenerator.generate(request);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(captcha.getImage(), "png", byteArrayOutputStream);
        return ResponseEntity.ok(Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray()));
    }

    @GetMapping("/{type}")
    public void image(HttpServletRequest request, HttpServletResponse response, @PathVariable String type) throws Exception {
        VerificationProcessor<?> verificationProcessor = verificationProcessors
                .parallelStream()
                .filter(processor -> type.equals(processor.verificationType()))
                .findFirst()
                .orElseThrow(() -> new VerificationException(String.format("无效的验证码类型%s", type)));
        verificationProcessor.send(request, response);
    }
}
