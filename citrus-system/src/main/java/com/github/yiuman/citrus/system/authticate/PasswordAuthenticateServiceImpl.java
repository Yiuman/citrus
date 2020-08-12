package com.github.yiuman.citrus.system.authticate;

import com.github.yiuman.citrus.security.authenticate.AuthenticateService;
import com.github.yiuman.citrus.security.verify.VerificationProcessor;
import com.github.yiuman.citrus.security.verify.captcha.Captcha;
import com.github.yiuman.citrus.support.utils.WebUtils;
import com.github.yiuman.citrus.system.cache.UserOnlineCache;
import com.github.yiuman.citrus.system.dto.UserOnlineInfo;
import com.github.yiuman.citrus.system.entity.User;
import com.github.yiuman.citrus.system.service.RbacMixinService;
import com.github.yiuman.citrus.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
public class PasswordAuthenticateServiceImpl implements AuthenticateService, UserDetailsService {

    private static final String SUPPORT_MODE = "password";

    private final RbacMixinService rbacMixinService;

    private final PasswordEncoder passwordEncoder;

    private final VerificationProcessor<Captcha> verificationProcessor;

    public PasswordAuthenticateServiceImpl(
            RbacMixinService rbacMixinService, PasswordEncoder passwordEncoder,
            VerificationProcessor<Captcha> verificationProcessor) {
        this.rbacMixinService = rbacMixinService;
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

        UserService userService = rbacMixinService.getUserService();
        User user = userService.getUserByLoginId(passwordLoginEntity.getLoginId())
                .orElseThrow(() -> new UsernameNotFoundException("找不到用户"));
        if (!passwordEncoder.matches(passwordLoginEntity.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("用户名或密码错误");
        }

        saveUserOnlineInfo(user);
        return new UsernamePasswordAuthenticationToken(user, user.getUuid());
    }


    @Override
    public Optional<Authentication> resolve(String token, String identity) {
        UserService userService = rbacMixinService.getUserService();
        UserOnlineCache userOnlineCache = userService.getUserOnlineCache();
        User user = userOnlineCache.find(identity);
        if (user == null) {
            user = userService.getUserByUuid(identity);
            saveUserOnlineInfo(user);
        }
        return Optional.of(new UsernamePasswordAuthenticationToken(user, token, null));
    }

    @Override
    public void logout(Authentication authentication) {
        final UserService userService = rbacMixinService.getUserService();
        userService.getUser(authentication).ifPresent(user -> userService.getUserOnlineCache().remove(user.getUuid()));
        //Nothing to do
    }

    @Override
    public String supportMode() {
        return SUPPORT_MODE;
    }

    private void saveUserOnlineInfo(User user) {
        UserOnlineInfo userOnlineInfo = UserOnlineInfo.newInstance(user);
        rbacMixinService.setUserOwnedInfo(userOnlineInfo);
        rbacMixinService.getUserService()
                .getUserOnlineCache()
                .save(user.getUuid(), userOnlineInfo);
    }

    @Override
    public UserDetails loadUserByUsername(String uuid) throws UsernameNotFoundException {
        UserOnlineInfo userOnlineInfo = rbacMixinService.getUserService().getUserOnlineCache().find(uuid);
        return new org.springframework.security.core.userdetails.User(userOnlineInfo.getUsername(), userOnlineInfo.getPassword(), null);
    }
}
