package com.github.yiuman.citrus.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yiuman.citrus.system.entity.Authority;
import com.github.yiuman.citrus.system.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author yiuman
 * @date 2020/3/31
 */
@Mapper
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据用户id及资源id查询用于角色数
     *
     * @param userId     用户ID
     * @param resourceId 资源ID
     * @return true/false
     */
    @Select("select count(1) from sys_user_role ur where ur.role_id in (select ra.role_id from sys_role_auth ra where ra.authority_id in " +
            "(select ar.authority_id from sys_authority_resource ar where ar.resource_id = #{resourceId}))")
    boolean hasPermission(@Param("userId") Long userId, @Param("resourceId") Long resourceId);

    /**
     * 查询角色所拥有的权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Select("select * from sys_authority sa where sa.authority_id in (select ra.authority_id from sys_role_auth ra where ra.role_id=#{roleId})")
    List<Authority> selectAuthoritiesByRoleId(@Param("roleId") Long roleId);

    /**
     * 查询用户拥有的权限
     *
     * @param userId 用户ID
     * @return 权限集合
     */
    @Select("select * from sys_authority sa where sa.authority_id in (select distinct(ra.authority_id) from sys_role_auth ra where ra.role_id in (select ur.role_id from sys_user_role ur where ur.user_ID = #{userId})")
    Set<Authority> selectAuthoritiesByUserId(@Param("userId") Long userId);
}
