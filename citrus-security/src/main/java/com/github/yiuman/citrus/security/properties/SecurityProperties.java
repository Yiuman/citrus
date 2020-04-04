package com.github.yiuman.citrus.security.properties;

/**
 * @author yiuman
 * @date 2020/3/22
 */
public class SecurityProperties {

    /**
     * 是否无状态应用
     */
    private boolean stateless = true;

    private String loginTypeName = SecurityConstants.DEFAULT_LOGIN_TYPE_NAME;

    private String authenticateEndpoint = SecurityConstants.AUTHENTICATE_ENDPOINT;

    private String verifyEndpointPrefix = SecurityConstants.VERIFY_ENDPOINT_PREFIX;

    public SecurityProperties() {
    }

    public boolean isStateless() {
        return stateless;
    }

    public void setStateless(boolean stateless) {
        this.stateless = stateless;
    }

    public String getLoginTypeName() {
        return loginTypeName;
    }

    public void setLoginTypeName(String loginTypeName) {
        this.loginTypeName = loginTypeName;
    }

    public String getAuthenticateEndpoint() {
        return authenticateEndpoint;
    }

    public void setAuthenticateEndpoint(String authenticateEndpoint) {
        this.authenticateEndpoint = authenticateEndpoint;
    }

    public String getVerifyEndpointPrefix() {
        return verifyEndpointPrefix;
    }

    public void setVerifyEndpointPrefix(String verifyEndpointPrefix) {
        this.verifyEndpointPrefix = verifyEndpointPrefix;
    }
}
