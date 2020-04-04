package com.github.yiuman.citrus.security.properties;

import com.github.yiuman.citrus.security.jwt.JwtProperties;
import com.github.yiuman.citrus.security.verify.VerifyProperties;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yiuman
 * @date 2020/3/22
 */
@Component
@Configurable
@ConfigurationProperties(prefix = "citrus")
public class CitrusProperties {

    private SecurityProperties security = new SecurityProperties();

    private VerifyProperties verify = new VerifyProperties();

    private JwtProperties jwt = new JwtProperties();

    public CitrusProperties() {
    }

    public SecurityProperties getSecurity() {
        return security;
    }

    public void setSecurity(SecurityProperties security) {
        this.security = security;
    }

    public VerifyProperties getVerify() {
        return verify;
    }

    public void setVerify(VerifyProperties verify) {
        this.verify = verify;
    }

    public JwtProperties getJwt() {
        return jwt;
    }

    public void setJwt(JwtProperties jwt) {
        this.jwt = jwt;
    }
}
