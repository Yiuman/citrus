package com.github.yiuman.citrus.system.mapper;

import com.github.yiuman.citrus.support.crud.mapper.CrudMapper;
import com.github.yiuman.citrus.system.entity.Role;
import com.github.yiuman.citrus.system.entity.User;
import com.github.yiuman.citrus.system.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * 用户角色DAO
 *
 * @author yiuman
 * @date 2020/4/7
 */
@Mapper
@Repository
public interface UserRoleMapper extends CrudMapper<UserRole> {

    /**
     * 根据ID获取用户的角色
     *
     * @param userId 用户ID
     * @return 角色集合
     */
    @Select("select * from sys_role sr , sys_user_role sur where sr.role_id = sur.role_id and sur.user_id = #{userId}")
    List<Role> getRolesByUserId(Long userId);

    /**
     * 根据角色ID找到用户的集合
     *
     * @param roleIds 角色ID
     * @return 用户集合
     */
    @Select("select * from sys_user su , sys_user_role sur where su.user_id = sur.user_id and sur.role_id in #{roleIds}")
    List<User> getUsersByRoleIds(Collection<Long> roleIds);

}
