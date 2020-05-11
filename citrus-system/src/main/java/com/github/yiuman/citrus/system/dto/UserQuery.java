package com.github.yiuman.citrus.system.dto;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.support.crud.QueryParam;
import com.github.yiuman.citrus.support.crud.QueryParamHandler;
import com.github.yiuman.citrus.system.entity.UserRole;
import com.github.yiuman.citrus.system.mapper.UserRoleMapper;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yiuman
 * @date 2020/4/6
 */
@Data
public class UserQuery {

    @QueryParam(type = "like")
    private String username;

    @QueryParam(handler = UserQueryHandler.class)
    private List<Long> roleIds;

    @Component
    public static class UserQueryHandler implements QueryParamHandler {

        private final UserRoleMapper userRoleMapper;

        public UserQueryHandler(UserRoleMapper userRoleMapper) {
            this.userRoleMapper = userRoleMapper;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handle(QueryParam queryParam, Object object, Field field, QueryWrapper<?> queryWrapper) throws Exception {
            QueryWrapper<UserRole> query = Wrappers.query();
            ReflectionUtils.makeAccessible(field);
            List<Long> roleIds = (List<Long>) ReflectionUtils.getField(field, object);
            if (roleIds == null) {
                return;
            }
            query.in("role_id", roleIds);
            List<Long> userRoles = userRoleMapper.selectList(query)
                    .stream()
                    .map(UserRole::getUserId)
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(userRoles)) {
                userRoles = Collections.singletonList(0L);

            }

            queryWrapper.in(true, "user_id", userRoles);
        }
    }

}
