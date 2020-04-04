package com.github.yiuman.citrus.rbac.service;

import com.github.yiuman.citrus.rbac.entity.User;
import com.github.yiuman.citrus.security.authenticate.AuthenticateService;
import com.github.yiuman.citrus.security.authenticate.LoginEntity;
import com.github.yiuman.citrus.security.verify.VerificationProcessor;
import com.github.yiuman.citrus.security.verify.captcha.Captcha;
import com.github.yiuman.citrus.support.utils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * 认证服务类实现
 *
 * @author yiuman
 * @date 2020/3/30
 */
@Service
public class PasswordAuthenticateService implements AuthenticateService {

    private static final String SUPPORT_MODE = "password";

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final VerificationProcessor<Captcha> verificationProcessor;

    public PasswordAuthenticateService(UserService userService, PasswordEncoder passwordEncoder, VerificationProcessor<Captcha> verificationProcessor) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.verificationProcessor = verificationProcessor;
    }

    @Override
    public Authentication authenticate(Object object) {
        LoginEntity loginEntity = (LoginEntity) supportEntityType().cast(object);

        HttpServletRequest request = WebUtils.getRequest();
        verificationProcessor.validate(request);

        User user = userService.getUserByLoginId(loginEntity.getLoginId())
                .orElseThrow(() -> new UsernameNotFoundException("找不到用户"));
        if (!passwordEncoder.matches(loginEntity.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("用户名或密码错误");
        }

        return new UsernamePasswordAuthenticationToken(user, user.getUuid());
    }

    @Override
    public Optional<Authentication> resolve(String token, String identity) {
        User userByUUID = userService.getUserByUUID(identity);
        return Optional.of(new UsernamePasswordAuthenticationToken(userByUUID, token, null));
    }

    @Override
    public void logout(Authentication authentication) {
        //Nothing to do
    }

    @Override
    public String supportMode() {
        return SUPPORT_MODE;
    }

    @Override
    public Class<?> supportEntityType() {
        return LoginEntity.class;
    }

}
