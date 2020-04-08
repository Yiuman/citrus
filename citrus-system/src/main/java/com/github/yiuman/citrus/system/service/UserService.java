package com.github.yiuman.citrus.system.service;

import com.github.yiuman.citrus.system.dto.UserDto;
import com.github.yiuman.citrus.system.entity.User;
import com.github.yiuman.citrus.system.entity.UserRole;
import com.github.yiuman.citrus.system.mapper.UserMapper;
import com.github.yiuman.citrus.support.crud.BaseDtoCrudService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户逻辑层
 *
 * @author yiuman
 * @date 2020/3/31
 */
@Service
public class UserService extends BaseDtoCrudService<UserMapper, User, UserDto, Long> {

    private final UserRoleService userRoleService;

    public UserService(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @Override
    public void afterSave(UserDto entity)  {
        //先删除就的数据
        userRoleService.removeByUserIdAndOrganId(entity.getUserId(),entity.getOrganId());
        List<UserRole> collect = entity.getRoleIds().parallelStream().map(roleId -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(entity.getUserId());
            userRole.setRoleId(roleId);
            userRole.setOrganId(entity.getOrganId());
            return userRole;
        }).collect(Collectors.toList());

        userRoleService.saveBatch(collect);
    }

    public User getUserByUUID(String uuid) {
        return getBaseMapper().getUserByUUID(uuid);
    }

    public Optional<User> getUserByLoginId(String loginId) {
        return Optional.ofNullable(getBaseMapper().getUserByLoginId(loginId));
    }

    /**
     * 根据身份认证对象获取User实体
     * @param authentication Security身份认证信息
     * @return Optional<User>
     */
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
