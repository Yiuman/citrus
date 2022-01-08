package com.github.yiuman.citrus.security.jwt;

import lombok.Data;

import java.util.Map;

/**
 * Token实体
 *
 * @author yiuman
 * @date 2020/9/6
 */
@Data
public class JwtToken {

    /**
     * 生成的Token
     */
    private String token;

    /**
     * 过期时间
     */
    private Long expireTimestamp;

    /**
     * 扩展信息
     */
    private Map<String, Object> extension;

    public JwtToken(String token, Long expireTimestamp) {
        this.token = token;
        this.expireTimestamp = expireTimestamp;
    }

}
