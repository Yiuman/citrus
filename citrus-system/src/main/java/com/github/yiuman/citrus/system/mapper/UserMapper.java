package com.github.yiuman.citrus.system.mapper;

import com.github.yiuman.citrus.support.crud.mapper.CrudMapper;
import com.github.yiuman.citrus.system.entity.Organization;
import com.github.yiuman.citrus.system.entity.Role;
import com.github.yiuman.citrus.system.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author yiuman
 * @date 2020/3/23
 */
@Mapper
@Repository
public interface UserMapper extends CrudMapper<User> {

    /**
     * 根据UUID查找用户
     *
     * @param uuid UUID
     * @return 用户实例
     */
    @Select("select * from sys_user user where user.uuid = #{uuid}")
    User getUserByUuid(@Param("uuid") String uuid);

    /**
     * 根据登录ID获取用户
     *
     * @param loginId 登录ID
     * @return 用户实例
     */
    @Select("select * from sys_user user where user.login_id = #{loginId}")
    User getUserByLoginId(@Param("loginId") String loginId);

    /**
     * 获取用户的所属角色
     *
     * @param userId 用户ID
     * @return 角色集合
     */
    @Select("select * from sys_role role where role.role_id in (select ur.role_id from sys_user_role ur where ur.user_id = #{userId})")
    List<Role> getRolesByUserId(Long userId);

    /**
     * 获取用户的所属角色
     *
     * @param userIds 用户ID
     * @return 角色集合
     */
    @Select("select * from sys_role role where role.role_id in (select ur.role_id from sys_user_role ur where ur.user_id in #{userId})")
    List<Role> getRolesByUserIds(Collection<Long> userIds);

    /**
     * 获取用户的所属机构
     *
     * @param userId 用户ID
     * @return 机构集合
     */
    @Select("select * from sys_organ organ where organ.organ_id in (select ur.organ_id from sys_user_role ur where ur.user_id = #{userId})")
    List<Organization> getOrgansByUserId(Long userId);

    /**
     * 获取用户的所属机构
     *
     * @param userIds 用户ID
     * @return 机构集合
     */
    @Select("select * from sys_organ organ where organ.organ_id in (select ur.organ_id from sys_user_role ur where ur.user_id in #{userId})")
    List<Organization> getOrgansByUserIds(Collection<Long> userIds);
}