package com.github.yiuman.citrus.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yiuman.citrus.system.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author yiuman
 * @date 2020/3/31
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select count(1) from UserRole ur where ur.roleId in (select ra.roleId from RoleAuthority ra where ra.authorityId in " +
            "(select ar.authorityId from sys_authority_resource ar where ar.resourceId = ${resourceId}))")
    boolean hasPermission(@Param("userId")Long userId,@Param("resourceId") String resourceId);
}
