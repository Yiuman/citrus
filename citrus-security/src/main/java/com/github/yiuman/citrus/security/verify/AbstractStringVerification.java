package com.github.yiuman.citrus.security.verify;

import java.time.LocalDateTime;

/**
 * 字符串类型的验证抽象类
 *
 * @author yiuman
 * @date 2020/3/22
 */
public class AbstractStringVerification implements Verification<String> {

    private String code;

    /**
     * Default 60s overdue
     */
    private long expireTime = 60;

    public AbstractStringVerification(String code) {
        this.code = code;
    }

    public AbstractStringVerification(String code, long expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    @Override
    public String getValue() {
        return code;
    }

    @Override
    public String getVerificationType() {
        return getClass().getSimpleName().toLowerCase();
    }

    @Override
    public LocalDateTime validTimeInSeconds() {
        return LocalDateTime.now().plusSeconds(expireTime);
    }
}
