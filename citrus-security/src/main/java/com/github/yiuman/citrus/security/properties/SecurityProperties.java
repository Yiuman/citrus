package com.github.yiuman.citrus.security.properties;

/**
 * @author yiuman
 * @date 2020/3/22
 */
public class SecurityProperties {

    private String authenticateEndpoint = SecurityConstants.AUTHENTICATE_ENDPOINT;

    private String verifyEndpointPrefix = SecurityConstants.VERIFY_ENDPOINT_PREFIX;

    private String[] excludedUris = new String[]{};

    public SecurityProperties() {
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

    public String[] getExcludedUris() {
        return excludedUris;
    }

    public void setExcludedUris(String[] excludedUris) {
        this.excludedUris = excludedUris;
    }
}
