package com.github.yiuman.citrus.system.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.security.authenticate.NoPermissionException;
import com.github.yiuman.citrus.support.crud.mapper.CrudMapper;
import com.github.yiuman.citrus.support.crud.query.builder.QueryBuilders;
import com.github.yiuman.citrus.support.crud.service.BaseDtoService;
import com.github.yiuman.citrus.support.exception.RestException;
import com.github.yiuman.citrus.support.http.ResponseStatusCode;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import com.github.yiuman.citrus.system.dto.UserDto;
import com.github.yiuman.citrus.system.dto.UserOnlineInfo;
import com.github.yiuman.citrus.system.entity.*;
import com.github.yiuman.citrus.system.mapper.RoleMapper;
import com.github.yiuman.citrus.system.mapper.UserMapper;
import com.github.yiuman.citrus.system.mapper.UserOrganMapper;
import com.github.yiuman.citrus.system.mapper.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户逻辑层
 *
 * @author yiuman
 * @date 2020/3/31
 */
@Service
@RequiredArgsConstructor
public class UserService extends BaseDtoService<User, Long, UserDto> {

    /**
     * 匿名登录的认证对象principal
     */
    private static final String ANONYMOUS = "anonymousUser";
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final UserOrganMapper userOrganMapper;
    private final OrganService organService;

    @Override
    public boolean beforeSave(UserDto entity) {
        if (Objects.isNull(entity.getUserId())) {
            //若是新增则添加默认密码及默认版本号
            entity.setPassword(passwordEncoder.encode("123456"));
            entity.setVersion(1);
            entity.setUuid(UUID.randomUUID().toString().replace("-", ""));
        }

        return true;
    }

    @Override
    public void afterSave(UserDto entity) {
        //保存组织机构信息
        List<Long> organIds = entity.getOrganIds();
        if (CollectionUtils.isEmpty(organIds)) {
            organIds = Collections.singletonList(-1L);
        }
        //先删除用户旧的角色部门数据
        userRoleMapper.delete(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, entity.getUserId()));
        userOrganMapper.delete(Wrappers.<UserOrgan>lambdaQuery().eq(UserOrgan::getUserId, entity.getUserId()));

        organIds.forEach(LambdaUtils.consumerWrapper(organId -> {
            userOrganMapper.saveEntity(new UserOrgan(entity.getUserId(), organId));
            if (CollUtil.isNotEmpty(entity.getRoleIds())) {
                //保存组织机构角色数据
                List<UserRole> userRoles = entity.getRoleIds().stream().map(roleId -> {
                    UserRole userRole = new UserRole();
                    userRole.setUserId(entity.getUserId());
                    userRole.setRoleId(roleId);
                    userRole.setOrganId(organId);
                    return userRole;
                }).collect(Collectors.toList());
                userRoleMapper.saveBatch(userRoles);
            }

        }));
    }


    @Override
    protected CrudMapper<User> getBaseMapper() {
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
        Object principal;
        //匿名不给进
        if (Objects.isNull(authentication) || ANONYMOUS.equals(principal = authentication.getPrincipal())) {
            return Optional.empty();
        }

        User user = null;
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

    public UserOnlineInfo getCurrentUserOnlineInfo() {
        User currentUser = getCurrentUser().orElseThrow(NoPermissionException::new);
        return UserOnlineInfo.newInstance(currentUser);

    }

    /**
     * 获取当前用户的组织机构
     *
     * @param userId 用户ID
     * @return 组织机构的集合
     */
    public List<Organization> getOrgansByUser(Long userId) {
        return userMapper.getOrgansByUserId(userId);
    }

    public List<Organization> getCurrentUserOrgans() {
        Optional<User> currentUser = getCurrentUser();
        if (!currentUser.isPresent()) {
            throw new NoPermissionException();
        }

        return getUserOrgansByUserId(currentUser.get().getUserId());
    }

    public List<Role> getRolesByUserId(Long userId) {
        return userMapper.getRolesByUserId(userId);
    }

    public List<Role> getRolesByUserIds(Collection<Long> userIds) {
        return userMapper.getRolesByUserIds(userIds);
    }

    public List<Organization> getUserOrgansByUserId(Long userId) {
        return userOrganMapper.getOrgansByUserId(userId);
    }

    public void updatePassword(String oldPassword, String newPassword) throws Exception {
        UserOnlineInfo currentUserOnlineInfo = getCurrentUserOnlineInfo();
        if (!passwordEncoder.encode(oldPassword).equals(currentUserOnlineInfo.getPassword())) {
            throw new RestException("The original passwords do not match", ResponseStatusCode.BAD_REQUEST);
        }
        UserDto userDto = get(currentUserOnlineInfo.getUserId());
        userDto.setPassword(passwordEncoder.encode(newPassword));
        save(userDto);
        resetUserOnlineInfo();
    }

    public void saveProfile(UserDto entity) {
        userMapper.updateById(dtoToEntity().apply(entity));
        resetUserOnlineInfo();
    }

    public void resetUserOnlineInfo() {
        UserOnlineInfo currentUserOnlineInfo = getCurrentUserOnlineInfo();
        BeanUtils.copyProperties(get(currentUserOnlineInfo.getUserId()), currentUserOnlineInfo);
    }

    public List<User> getUsersByRoleIds(Collection<Long> roleIds) {
        return userRoleMapper.getUsersByRoleIds(roleIds);
    }

    public List<User> getUsersByDeptIds(List<Long> deptIds) {
        return userOrganMapper.getUsersByDeptIds(deptIds);
    }

    @Override
    public boolean beforeRemove(UserDto entity) {
        userOrganMapper.delete(Wrappers.<UserOrgan>lambdaQuery().eq(UserOrgan::getUserId, entity.getUserId()));
        userRoleMapper.delete(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, entity.getUserId()));
        return super.beforeRemove(entity);
    }

    public List<Organization> getOrgansByUserIds(Set<Long> userIds) {
        return userMapper.getOrgansByUserIds(userIds);
    }

    public List<UserRole> getUserRolesByUserIds(Set<Long> userIds) {
        List<UserRole> userRoles = userRoleMapper.selectList(Wrappers.<UserRole>lambdaQuery().in(UserRole::getUserId, userIds));
        Set<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
        Map<Long, Role> roleMap = roleMapper.selectBatchIds(roleIds).stream().collect(Collectors.toMap(Role::getRoleId, role -> role));
        userRoles.forEach(userRole -> userRole.setRole(roleMap.get(userRole.getRoleId())));
        return userRoles;
    }

    public List<UserOrgan> getUserOrgansByUserIds(Set<Long> userIds) {
        List<UserOrgan> userOrgans = userOrganMapper.selectList(Wrappers.<UserOrgan>lambdaQuery().in(UserOrgan::getUserId, userIds));
        Set<Long> organIds = userOrgans.stream().map(UserOrgan::getOrganId).collect(Collectors.toSet());
        Map<Long, Organization> organizationMap = organService.list(QueryBuilders.<Organization>lambda().in(Organization::getOrganId, organIds).toQuery())
                .stream().collect(Collectors.toMap(Organization::getOrganId, organization -> organization));
        userOrgans.forEach(userOrgan -> userOrgan.setOrgan(organizationMap.get(userOrgan.getOrganId())));
        return userOrgans;
    }
}
