package com.github.yiuman.citrus.security.properties;

/**
 * 系统安全认证配置
 *
 * @author yiuman
 * @date 2020/3/22
 */
public class SecurityProperties {

    /**
     * 身份认证的端点
     */
    private String authenticateEndpoint = SecurityConstants.AUTHENTICATE_ENDPOINT;

    /**
     * 登出端点
     */
    private String logoutEndpoint = SecurityConstants.LOGOUT_ENDPOINT;

    /**
     * 验证端点前缀
     */
    private String verifyEndpointPrefix = SecurityConstants.VERIFY_ENDPOINT_PREFIX;

    /**
     * 需排除的url
     */
    private String[] excludedUris = new String[]{};

    public SecurityProperties() {
    }

    public String getAuthenticateEndpoint() {
        return authenticateEndpoint;
    }

    public void setAuthenticateEndpoint(String authenticateEndpoint) {
        this.authenticateEndpoint = authenticateEndpoint;
    }

    public String getLogoutEndpoint() {
        return logoutEndpoint;
    }

    public void setLogoutEndpoint(String logoutEndpoint) {
        this.logoutEndpoint = logoutEndpoint;
    }

    public String getVerifyEndpointPrefix() {
        return verifyEndpointPrefix;
    }

    public void setVerifyEndpointPrefix(String verifyEndpointPrefix) {
        this.verifyEndpointPrefix = verifyEndpointPrefix;
    }

    public String[] getExcludedUris() {
        return excludedUris;
    }

    public void setExcludedUris(String[] excludedUris) {
        this.excludedUris = excludedUris;
    }
}
