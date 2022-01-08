package com.github.yiuman.citrus.system.authticate;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yiuman.citrus.security.authenticate.AuthenticateService;
import com.github.yiuman.citrus.security.verify.VerificationProcessor;
import com.github.yiuman.citrus.security.verify.captcha.Captcha;
import com.github.yiuman.citrus.support.utils.WebUtils;
import com.github.yiuman.citrus.system.dto.UserOnlineInfo;
import com.github.yiuman.citrus.system.entity.Resource;
import com.github.yiuman.citrus.system.entity.User;
import com.github.yiuman.citrus.system.hook.AccessPointer;
import com.github.yiuman.citrus.system.service.RbacMixinService;
import com.github.yiuman.citrus.system.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

/**
 * 认证服务类实现
 *
 * @author yiuman
 * @date 2020/3/30
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PasswordAuthenticateServiceImpl implements AuthenticateService, UserDetailsService {

    private static final String SUPPORT_MODE = "password";

    private final RbacMixinService rbacMixinService;

    private final AccessPointer accessPointer;

    private final PasswordEncoder passwordEncoder;

    private final VerificationProcessor<Captcha> verificationProcessor;

    @Value("${citrus.verify.enable:true}")
    private boolean enableVerify;

    @Override
    public Authentication authenticate(HttpServletRequest request) {
        if (enableVerify) {
            verificationProcessor.validate(request);
        }

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

        if (StringUtils.isBlank(user.getUuid())) {
            throw new BadCredentialsException("此用户目前不存在密匙");
        }

        try {
            accessPointer.doPoint(request, user, new Resource("登录"));
        } catch (Exception ignore) {
        }

        return new UsernamePasswordAuthenticationToken(user, user.getUuid());
    }


    @Override
    public Optional<Authentication> resolve(String token, String identity) {
        UserService userService = rbacMixinService.getUserService();
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

    @Override
    public UserDetails loadUserByUsername(String uuid) throws UsernameNotFoundException {
        User userByUuid = rbacMixinService.getUserService().getUserByUuid(uuid);
        UserOnlineInfo userOnlineInfo = Objects.nonNull(userByUuid)
                ? UserOnlineInfo.newInstance(userByUuid)
                : UserOnlineInfo.anonymous();
        return new org.springframework.security.core.userdetails.User(userOnlineInfo.getUsername(), userOnlineInfo.getPassword(), Collections.emptyList());
    }
}
