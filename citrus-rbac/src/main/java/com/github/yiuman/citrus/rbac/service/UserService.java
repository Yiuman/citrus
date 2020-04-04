package com.github.yiuman.citrus.rbac.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yiuman.citrus.rbac.dto.UserDto;
import com.github.yiuman.citrus.rbac.entity.User;
import com.github.yiuman.citrus.rbac.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
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
public class UserService extends ServiceImpl<UserMapper, User> {

    public Long save(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        saveOrUpdate(user);
        return user.getUserId();
    }

    public UserDto getUser(Long id) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(baseMapper.selectById(id), userDto);
        return userDto;
    }

    public List<UserDto> selectAll() {
        return list().parallelStream().map(user -> {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            return userDto;
        }).collect(Collectors.toList());
    }

    public User getUserByUUID(String uuid) {
        return getBaseMapper().getUserByUUID(uuid);
    }

    public Optional<User> getUserByLoginId(String loginId) {
        return Optional.ofNullable(getBaseMapper().getUserByLoginId(loginId));
    }
}
