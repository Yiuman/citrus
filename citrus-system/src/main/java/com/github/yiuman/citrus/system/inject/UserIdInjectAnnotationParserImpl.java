package com.github.yiuman.citrus.system.inject;

import com.github.yiuman.citrus.support.inject.InjectAnnotationParser;
import com.github.yiuman.citrus.system.entity.User;
import com.github.yiuman.citrus.system.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * UserId用户Id注入解析器实现
 *
 * @author yiuman
 * @date 2020/7/23
 */
@Component
public class UserIdInjectAnnotationParserImpl implements InjectAnnotationParser<UserId> {

    private final UserService userService;

    public UserIdInjectAnnotationParserImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Object parse(UserId annotation) {
        Optional<User> currentUser = userService.getCurrentUser();
        return currentUser.<Object>map(User::getUserId).orElse(null);
    }
}
