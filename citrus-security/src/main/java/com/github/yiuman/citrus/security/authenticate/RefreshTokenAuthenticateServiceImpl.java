package com.github.yiuman.citrus.security.authenticate;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.github.yiuman.citrus.security.jwt.JwtUtils;
import com.github.yiuman.citrus.support.utils.WebUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * refreshToken认证服务实现
 *
 * @author yiuman
 * @date 2022/1/17
 */
@Service
public class RefreshTokenAuthenticateServiceImpl implements AuthenticateService {

    public RefreshTokenAuthenticateServiceImpl() {
    }

    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    @Override
    public Authentication authenticate(HttpServletRequest request) throws AuthenticationException {
        try {
            RefreshTokenVM refreshTokenVM = WebUtils.requestDataBind(RefreshTokenVM.class, request);
            Assert.isTrue(
                    StrUtil.isNotBlank(refreshTokenVM.getRefreshToken()),
                    () -> new AuthenticationServiceException("request parameter error")
            );
            return new UsernamePasswordAuthenticationToken(null, JwtUtils.getIdentityClaimsValue(refreshTokenVM.getRefreshToken()));
        } catch (Exception exception) {
            //此处可能出现实体入参校验异常
            throw new AuthenticationServiceException(exception.getMessage());
        }

    }

    @Override
    public Optional<Authentication> resolve(String token, String identity) {
        throw new AuthenticationServiceException("refreshToken cannot be used for other interfaces");
    }

    @Override
    public void logout(Authentication authentication) {
    }

    @Override
    public String supportMode() {
        return "refreshToken";
    }
}
