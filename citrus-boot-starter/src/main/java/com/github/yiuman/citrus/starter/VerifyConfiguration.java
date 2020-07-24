package com.github.yiuman.citrus.starter;

import com.github.yiuman.citrus.security.properties.CitrusProperties;
import com.github.yiuman.citrus.security.verify.*;
import com.github.yiuman.citrus.security.verify.captcha.Captcha;
import com.github.yiuman.citrus.security.verify.captcha.CaptchaProcessor;
import com.github.yiuman.citrus.security.verify.sms.SmsVerifyCode;
import com.github.yiuman.citrus.security.verify.sms.SmsVerifyCodeProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author yiuman
 * @date 2020/3/22
 */
@Configuration
@EnableConfigurationProperties(CitrusProperties.class)
@Import(SystemDefaultBeanConfiguration.class)
public class VerifyConfiguration {

    private final CitrusProperties citrusProperties;

    private final RedisTemplate<?, ?> redisTemplate;

    public VerifyConfiguration(CitrusProperties citrusProperties, RedisTemplate<?, ?> redisTemplate) {
        this.citrusProperties = citrusProperties;
        this.redisTemplate = redisTemplate;
    }

    @Bean
    @SuppressWarnings("unchecked")
    public VerificationRepository redisRepository() {
        if (VerifyProperties.Store.REDIS.equals(citrusProperties.getVerify().getStore())) {
            return new RedisVerificationRepository((RedisTemplate<String, Object>) redisTemplate);
        }
        return new SessionVerificationRepository();

    }

    @Bean("smsVerifyCodeProcessor")
    public VerificationProcessor<SmsVerifyCode> smsVerifyCodeProcessor() {
        return new SmsVerifyCodeProcessor(redisRepository(), citrusProperties.getVerify());
    }

    @Bean("captchaProcessor")
    public VerificationProcessor<Captcha> captchaProcessor() {
        return new CaptchaProcessor(redisRepository(), citrusProperties.getVerify());
    }

//    @Bean("base64Captcha")
//    public VerificationProcessor<Captcha> base64CaptchaProcessor() {
//        return new Base64CaptchaProcessor(redisRepository(), citrusProperties.getVerify());
//    }


}
