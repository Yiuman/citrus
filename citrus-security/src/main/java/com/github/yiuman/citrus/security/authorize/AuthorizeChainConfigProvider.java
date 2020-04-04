package com.github.yiuman.citrus.security.authorize;

import com.github.yiuman.citrus.security.properties.CitrusProperties;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * @author yiuman
 * @date 2020/3/30
 */
@Component
@Order(Integer.MAX_VALUE)
public class AuthorizeChainConfigProvider implements AuthorizeConfigProvider {

    private final CitrusProperties citrusProperties;

    public AuthorizeChainConfigProvider(CitrusProperties citrusProperties) {
        this.citrusProperties = citrusProperties;
    }


    @Override
    public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        config.antMatchers(citrusProperties.getSecurity().getAuthenticateEndpoint(),
                citrusProperties.getSecurity().getVerifyEndpointPrefix()+"/**"
        ).permitAll();
        config
                .anyRequest()
                .access("@authorizeChainManager.hasPermission(request, authentication)");
        return true;
    }
}
