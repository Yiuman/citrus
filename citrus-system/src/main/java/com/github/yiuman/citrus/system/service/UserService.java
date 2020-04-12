package com.github.yiuman.citrus.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yiuman.citrus.support.crud.service.BaseDtoCrudService;
import com.github.yiuman.citrus.system.dto.UserDto;
import com.github.yiuman.citrus.system.entity.User;
import com.github.yiuman.citrus.system.entity.UserOrgan;
import com.github.yiuman.citrus.system.entity.UserRole;
import com.github.yiuman.citrus.system.mapper.UserMapper;
import com.github.yiuman.citrus.system.mapper.UserOrganMapper;
import com.github.yiuman.citrus.system.mapper.UserRoleMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    /**
     * 用户角色mapper
     */
    private final UserRoleMapper userRoleMapper;

    /**
     * 用户组织机构mapper
     */
    private final UserOrganMapper userOrganMapper;

    public UserService(UserRoleMapper userRoleMapper, UserOrganMapper userOrganMapper) {
        this.userRoleMapper = userRoleMapper;
        this.userOrganMapper = userOrganMapper;
    }

    @Override
    public void afterSave(UserDto entity) {
        //保存组织机构信息
        List<Long> organIds = entity.getOrganIds();
        if (!CollectionUtils.isEmpty(organIds)) {
            organIds.forEach(organId -> {
                userOrganMapper.saveEntity(new UserOrgan(entity.getUserId(), organId));
                //先删除旧的角色数据
                userRoleMapper.delete(new QueryWrapper<UserRole>()
                        .eq("user_id", entity.getUserId())
                        .eq("organ_id", organId));
                //保存组织机构角色数据
                List<UserRole> userRoles = entity.getRoleIds().parallelStream().map(roleId -> {
                    UserRole userRole = new UserRole();
                    userRole.setUserId(entity.getUserId());
                    userRole.setRoleId(roleId);
                    userRole.setOrganId(organId);
                    return userRole;
                }).collect(Collectors.toList());
                userRoleMapper.saveBatch(userRoles);
            });
        }
    }

    public User getUserByUUID(String uuid) {
        return getBaseMapper().getUserByUUID(uuid);
    }

    public Optional<User> getUserByLoginId(String loginId) {
        return Optional.ofNullable(getBaseMapper().getUserByLoginId(loginId));
    }

    /**
     * 根据身份认证对象获取User实体
     *
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
