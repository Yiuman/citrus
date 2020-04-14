package com.github.yiuman.citrus.security.jwt;

/**
 * Jwt相关配置
 *
 * @author yiuman
 * @date 2020/3/16
 */
public class JwtProperties {

    /**
     * token请求头
     */
    private String tokenHeader = JwtConstants.TOKEN_HEADER;

    /**
     * token值的前缀，如Bearer （注意此处有空格）
     */
    private String tokenPrefix = JwtConstants.TOKEN_PREFIX;

    /**
     * JWT签名
     * 必须使用最少88位的Base64对该令牌进行编码
     */
    private String secret = JwtConstants.SECRET;

    /**
     * 身份认证的标识值，用于后面根据标识值解析出当前认证用户实例
     */
    private String identityKey = JwtConstants.IDENTITY_KEY;

    /**
     * 过期时间
     */
    private long tokenValidateInSeconds = JwtConstants.VALIDATE_IN_SECONDS;

    /**
     * 记住我的时间
     */
    private long rememberMeValidateInSeconds = JwtConstants.REMEMBER_ME_VALIDATE_IN_SECONDS;

    public JwtProperties() {
    }

    public String getTokenHeader() {
        return tokenHeader;
    }

    public void setTokenHeader(String tokenHeader) {
        this.tokenHeader = tokenHeader;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getIdentityKey() {
        return identityKey;
    }

    public void setIdentityKey(String identityKey) {
        this.identityKey = identityKey;
    }

    public long getTokenValidateInSeconds() {
        return tokenValidateInSeconds;
    }

    public void setTokenValidateInSeconds(long tokenValidateInSeconds) {
        this.tokenValidateInSeconds = tokenValidateInSeconds;
    }

    public long getRememberMeValidateInSeconds() {
        return rememberMeValidateInSeconds;
    }

    public void setRememberMeValidateInSeconds(long rememberMeValidateInSeconds) {
        this.rememberMeValidateInSeconds = rememberMeValidateInSeconds;
    }

    /**
     * JWT配置默认常量
     */
    interface JwtConstants {

        String TOKEN_HEADER = "Authorization";

        String TOKEN_PREFIX = "Bearer ";

        String SECRET = "ZmQ0ZGI5NjQ0MDQwY2I4MjMxY2Y3ZmI3MjdhN2ZmMjNhODViOTg1ZGE0NTBjMGM4NDA5NzYxMjdjOWMwYWRmZTBlZjlhNGY3ZTg4Y2U3YTE1ODVkZDU5Y2Y3OGYwZWE1NzUzNWQ2YjFjZDc0NGMxZWU2MmQ3MjY1NzJmNTE0MzI";

        String IDENTITY_KEY = "Identity";

        Long VALIDATE_IN_SECONDS = 604800L;

        Long REMEMBER_ME_VALIDATE_IN_SECONDS = 2592000L;
    }

}
