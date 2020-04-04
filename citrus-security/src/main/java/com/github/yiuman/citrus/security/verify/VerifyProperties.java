package com.github.yiuman.citrus.security.verify;

/**
 * @author yiuman
 * @date 2020/3/22
 */
public class VerifyProperties {

    private String store = "session";

    private int verifyCodeSize = 4;

    private int captchaWidth = 120;

    private int captchaHeight = 40;

    public VerifyProperties() {
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public int getVerifyCodeSize() {
        return verifyCodeSize;
    }

    public void setVerifyCodeSize(int verifyCodeSize) {
        this.verifyCodeSize = verifyCodeSize;
    }

    public int getCaptchaWidth() {
        return captchaWidth;
    }

    public void setCaptchaWidth(int captchaWidth) {
        this.captchaWidth = captchaWidth;
    }

    public int getCaptchaHeight() {
        return captchaHeight;
    }

    public void setCaptchaHeight(int captchaHeight) {
        this.captchaHeight = captchaHeight;
    }
}
