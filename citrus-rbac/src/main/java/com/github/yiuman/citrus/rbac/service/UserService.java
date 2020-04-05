package com.github.yiuman.citrus.rbac.service;

import com.github.yiuman.citrus.rbac.dto.UserDto;
import com.github.yiuman.citrus.rbac.entity.User;
import com.github.yiuman.citrus.rbac.mapper.UserMapper;
import com.github.yiuman.citrus.support.crud.BaseDtoCrudService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 用户逻辑层
 *
 * @author yiuman
 * @date 2020/3/31
 */
@Service
public class UserService extends BaseDtoCrudService<UserMapper, User, UserDto, Long> {

    public User getUserByUUID(String uuid) {
        return getBaseMapper().getUserByUUID(uuid);
    }

    public Optional<User> getUserByLoginId(String loginId) {
        return Optional.ofNullable(getBaseMapper().getUserByLoginId(loginId));
    }

    public Optional<User> getUser(Authentication authentication) {
        User user = null;
        Object principal = authentication.getPrincipal();
        //匿名不给进
        if ("anonymousUser".equals(principal)) {
            return Optional.empty();
        }

        if (principal instanceof User) {
            user = (User) principal;
        } else if (principal instanceof String) {
            user = getUserByUUID((String) principal);
        }

        return Optional.ofNullable(user);
    }
}
