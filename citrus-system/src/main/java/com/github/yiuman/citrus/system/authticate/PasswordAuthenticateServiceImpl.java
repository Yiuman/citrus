package com.github.yiuman.citrus.system.authticate;

import com.github.yiuman.citrus.security.authenticate.AuthenticateService;
import com.github.yiuman.citrus.security.verify.VerificationProcessor;
import com.github.yiuman.citrus.security.verify.captcha.Captcha;
import com.github.yiuman.citrus.support.utils.WebUtils;
import com.github.yiuman.citrus.system.entity.User;
import com.github.yiuman.citrus.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 认证服务类实现
 *
 * @author yiuman
 * @date 2020/3/30
 */
@Service
@Slf4j
public class PasswordAuthenticateServiceImpl implements AuthenticateService {

    private static final String SUPPORT_MODE = "password";

    private final UserService userService;


    private final PasswordEncoder passwordEncoder;

    private final VerificationProcessor<Captcha> verificationProcessor;

    public PasswordAuthenticateServiceImpl(UserService userService, PasswordEncoder passwordEncoder, VerificationProcessor<Captcha> verificationProcessor) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.verificationProcessor = verificationProcessor;
    }

    @Override
    public Authentication authenticate(HttpServletRequest request) {
        verificationProcessor.validate(request);

        PasswordLoginEntity passwordLoginEntity;
        try {
            passwordLoginEntity = WebUtils.bindDataAndValidate(PasswordLoginEntity.class, request);
        } catch (Exception e) {
            //此处可能出现实体入参校验异常
            throw new AuthenticationServiceException(e.getMessage());
        }

        User user = userService.getUserByLoginId(passwordLoginEntity.getLoginId())
                .orElseThrow(() -> new UsernameNotFoundException("找不到用户"));
        if (!passwordEncoder.matches(passwordLoginEntity.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("用户名或密码错误");
        }

        return new UsernamePasswordAuthenticationToken(user, user.getUuid());
    }


    @Override
    public Optional<Authentication> resolve(String token, String identity) {
        User user = userService.getUserByUuid(identity);
        return Optional.of(new UsernamePasswordAuthenticationToken(user, token, null));
    }

    @Override
    public void logout(Authentication authentication) {
        //Nothing to do
    }

    @Override
    public String supportMode() {
        return SUPPORT_MODE;
    }

}
