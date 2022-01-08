package com.github.yiuman.citrus.security.verify;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yiuman
 * @date 2020/3/22
 */
@Component
@ConfigurationProperties(prefix = "citrus.verify")
@Data
public class VerifyProperties {

    private String store = Store.SESSION;

    private boolean enable = true;

    private int verifyCodeSize = 4;

    private int captchaWidth = 120;

    private int captchaHeight = 40;

    public interface Store {

        String SESSION = "session";

        String REDIS = "redis";

    }
}
