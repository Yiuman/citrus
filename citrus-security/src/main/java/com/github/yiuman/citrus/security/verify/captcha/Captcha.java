package com.github.yiuman.citrus.security.verify.captcha;

import com.github.yiuman.citrus.security.verify.AbstractStringVerification;

import java.awt.image.BufferedImage;

/**
 * 图片验证码
 *
 * @author yiuman
 * @date 2020/3/22
 */
public class Captcha extends AbstractStringVerification {

    private BufferedImage image;

    public Captcha(String code, BufferedImage image) {
        super(code);
        this.image = image;
    }

    public Captcha(String code, long expireTime, BufferedImage image) {
        super(code, expireTime);
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
