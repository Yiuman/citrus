package com.github.yiuman.citrus.security.authenticate;

import com.github.yiuman.citrus.security.jwt.JwtProvider;
import com.github.yiuman.citrus.security.verify.VerificationException;
import com.github.yiuman.citrus.support.utils.ValidateUtils;
import com.github.yiuman.citrus.support.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 认证处理器实现
 *
 * @author yiuman
 * @date 2020/4/3
 */
@Service
public class AuthenticateProcessorImpl implements AuthenticateProcessor {

    /**
     * 认证模式参数KEY
     */
    private final static String AUTHENTICATION_MODE_PARAMETER_KEY = "mode";

    private final JwtProvider jwtProvider;

    /**
     * 认证服务类
     */
    private final List<AuthenticateService> authenticateServices;

    public AuthenticateProcessorImpl(JwtProvider jwtProvider, List<AuthenticateService> authenticateServices) {
        Assert.notNull(authenticateServices, "AuthenticateService must not be null");
        this.jwtProvider = jwtProvider;
        this.authenticateServices = authenticateServices;
    }

    @Override
    public AuthenticateService findByMode(String mode) throws AuthenticationException {
        if (StringUtils.isEmpty(mode)) {
            throw new AuthenticationServiceException("The Authenticate's parameter 'model' must not be null");
        }

        //找到对应模式的认证服务类
        return authenticateServices.parallelStream()
                .filter(authenticateService -> mode.equals(authenticateService.supportMode()))
                .findFirst()
                .orElseThrow(() ->
                        new AuthenticationServiceException(String.format("Cannot found Authenticate's model of %s", mode)));
    }

    @Override
    public Object covertRequestModeEntity(Class<?> entityClass, HttpServletRequest request) {
        //构造认证模式实体
        final Object supportEntity = WebUtils.requestDataBind(entityClass,request);
        //校验实体参数
        ValidateUtils.validateEntityAndThrows(supportEntity,result->new VerificationException(result.getMessage()));
        return supportEntity;
    }

    @Override
    public Authentication authenticate(final HttpServletRequest request) throws AuthenticationException {
        AuthenticateService matchingService = findByMode(request.getParameter(AUTHENTICATION_MODE_PARAMETER_KEY));
        return matchingService.authenticate(covertRequestModeEntity(matchingService.supportEntityType(), request));

    }

    @Override
    public String token(HttpServletRequest request) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHENTICATION_MODE_PARAMETER_KEY, request.getParameter(AUTHENTICATION_MODE_PARAMETER_KEY));
        return jwtProvider.generateToken(authenticate(request), claims);
    }

    @Override
    public Optional<Authentication> resolve(HttpServletRequest request) {
        String token = jwtProvider.resolveToken(request);
        if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
            Claims claims = jwtProvider.getClaims(token);
            String mode = (String) claims.get(AUTHENTICATION_MODE_PARAMETER_KEY);
            String identity = (String) claims.get(jwtProvider.getIdentityKey());
            AuthenticateService service = findByMode(mode);
            return service.resolve(token, identity);
        }
        return Optional.empty();
    }


}
