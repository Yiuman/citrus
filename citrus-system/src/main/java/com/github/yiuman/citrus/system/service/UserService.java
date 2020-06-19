package com.github.yiuman.citrus.system.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.support.crud.service.BaseDtoService;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import com.github.yiuman.citrus.system.dto.UserDto;
import com.github.yiuman.citrus.system.entity.Organization;
import com.github.yiuman.citrus.system.entity.Role;
import com.github.yiuman.citrus.system.entity.User;
import com.github.yiuman.citrus.system.entity.UserRole;
import com.github.yiuman.citrus.system.mapper.UserMapper;
import com.github.yiuman.citrus.system.mapper.UserRoleMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
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
public class UserService extends BaseDtoService<User, Long, UserDto> {

    /**
     * 匿名登录的认证对象principal
     */
    private final static String ANONYMOUS = "anonymousUser";

    private final UserMapper userMapper;

    /**
     * 用户角色mapper
     */
    private final UserRoleMapper userRoleMapper;

    /**
     * 用户组织机构mapper
     */
    public UserService(UserMapper userMapper, UserRoleMapper userRoleMapper) {
        this.userMapper = userMapper;
        this.userRoleMapper = userRoleMapper;
    }

    @Override
    public void afterSave(UserDto entity) {
        //保存组织机构信息
        List<Long> organIds = entity.getOrganIds();
        if (CollectionUtils.isEmpty(organIds)) {
            organIds = Collections.singletonList(-1L);
        }
        //先删除用户旧的角色部门数据
        userRoleMapper.delete(Wrappers.<UserRole>query()
                .eq("user_id", entity.getUserId())
                .in("organ_id", organIds));

        organIds.forEach(LambdaUtils.consumerWrapper(organId -> {
            //保存组织机构角色数据
            List<UserRole> userRoles = entity.getRoleIds().stream().map(roleId -> {
                UserRole userRole = new UserRole();
                userRole.setUserId(entity.getUserId());
                userRole.setRoleId(roleId);
                userRole.setOrganId(organId);
                return userRole;
            }).collect(Collectors.toList());
            boolean b = userRoleMapper.saveBatch(userRoles);
        }));
    }


    @Override
    protected BaseMapper<User> getBaseMapper() {
        return userMapper;
    }

    public User getUserByUuid(String uuid) {
        return userMapper.getUserByUuid(uuid);
    }

    public Optional<User> getUserByLoginId(String loginId) {
        return Optional.ofNullable(userMapper.getUserByLoginId(loginId));
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
        if (ANONYMOUS.equals(principal)) {
            return Optional.empty();
        }

        if (principal instanceof User) {
            user = (User) principal;
        } else if (principal instanceof String) {
            user = getUserByUuid((String) principal);
        }

        return Optional.ofNullable(user);
    }

    public List<Role> getRoleByUser(UserDto userDto) {
        return userMapper.getRolesByUserId(userDto.getUserId());
    }

    /**
     * 获取当前用户实例
     *
     * @return User的Optional
     */
    public Optional<User> getCurrentUser() {
        return getUser(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * 获取当前用户的组织机构
     *
     * @param userId 用户ID
     * @return 组织机构的集合
     */
    public List<Organization> getOrganByUser(Long userId) {
        return userMapper.getOrgansByUserId(userId);
    }

}
