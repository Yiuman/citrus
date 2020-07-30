package com.github.yiuman.citrus.system.mapper;

import com.github.yiuman.citrus.support.crud.mapper.CrudMapper;
import com.github.yiuman.citrus.system.entity.Role;
import com.github.yiuman.citrus.system.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户角色DAO
 *
 * @author yiuman
 * @date 2020/4/7
 */
@Mapper
public interface UserRoleMapper extends CrudMapper<UserRole> {

    @Select("select * from sys_role sr , sys_user_role sur where sr.role_id = sur.role_id and sur.user_id = #{userId}")
    List<Role> getRolesByUserId(Long userId);
}
