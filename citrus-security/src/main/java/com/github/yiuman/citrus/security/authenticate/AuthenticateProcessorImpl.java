package com.github.yiuman.citrus.security.authenticate;

import com.github.yiuman.citrus.security.jwt.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.support.AbstractMultipartHttpServletRequest;

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
@Slf4j
public class AuthenticateProcessorImpl implements AuthenticateProcessor {

    /**
     * 认证模式参数KEY
     */
    private final static String AUTHENTICATION_MODE_PARAMETER_KEY = "mode";

    /**
     * 认证服务类
     */
    private final List<AuthenticateService> authenticateServices;

    public AuthenticateProcessorImpl(List<AuthenticateService> authenticateServices) {
        Assert.notNull(authenticateServices, "AuthenticateService must not be null");
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
    public Authentication authenticate(HttpServletRequest request) throws AuthenticationException {
        return findByMode(request.getParameter(AUTHENTICATION_MODE_PARAMETER_KEY)).authenticate(request);
    }

    @Override
    public String token(HttpServletRequest request) {
        HttpServletRequest actualRequest = request;
        if (!(request instanceof AbstractMultipartHttpServletRequest)) {
            try {
                //此处非Multipart就构造一个Json的RequestWrapper 用与适配多种请求方式
                actualRequest = new JsonServletRequestWrapper(request);
            } catch (Exception e) {
                throw new AuthenticationServiceException("BAD REQUEST");
            }
        }

        Map<String, Object> claims = new HashMap<>(16);
        claims.put(AUTHENTICATION_MODE_PARAMETER_KEY, actualRequest.getParameter(AUTHENTICATION_MODE_PARAMETER_KEY));
        Authentication authenticate = authenticate(actualRequest);
        return JwtUtils.generateToken((String) authenticate.getCredentials(), claims);
    }

    @Override
    public Optional<Authentication> resolve(HttpServletRequest request) {
        String token = JwtUtils.resolveToken(request);
        if (StringUtils.hasText(token) && JwtUtils.validateToken(token)) {
            Claims claims = JwtUtils.getClaims(token);
            String mode = (String) claims.get(AUTHENTICATION_MODE_PARAMETER_KEY);
            String identity = (String) claims.get(JwtUtils.getIdentityKey());
            try {
                AuthenticateService service = findByMode(mode);
                return service.resolve(token, identity);
            } catch (Exception e) {
                return Optional.empty();
            }

        }
        return Optional.empty();
    }


}
