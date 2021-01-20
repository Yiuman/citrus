package com.github.yiuman.citrus.security.verify;

import java.time.LocalDateTime;

/**
 * 验证接口,用于抽象系统中的验证（如图片验证码，短信验证码等）
 *
 * @author yiuman
 * @date 2020/3/22
 */
public interface Verification<T> {

    /**
     * 获取验证码对象
     *
     * @return 获取验证实体值
     */
    T getValue();

    /**
     * 验证码类型
     *
     * @return 验证码类型标识字符串 如验证码为"captcha"
     */
    String getVerificationType();

    /**
     * 有效时间（单位秒）
     *
     * @return 有效的验证时间
     */
    LocalDateTime validTimeInSeconds();
}