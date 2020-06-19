package com.github.yiuman.citrus.system.mapper;

import com.github.yiuman.citrus.support.crud.mapper.CrudMapper;
import com.github.yiuman.citrus.system.entity.AuthorityResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * 权限资源配置Mapper
 *
 * @author yiuman
 * @date 2020/4/11
 */
@Mapper
public interface AuthorityResourceMapper extends CrudMapper<AuthorityResource> {

    /**
     * 根据用户ID查询用户的权限资源集合，此处部分操作资源与菜单访问 资源
     *
     * @param userId 用户ID
     * @return 权限资源集合
     */
    @Select("select * from sys_auth_resource ar where ar.authority_id in " +
            "(select sa.authority_id from sys_authority sa " +
            "   where sa.authority_id in " +
            "   (select distinct(ra.authority_id) from sys_role_auth ra where ra.role_id in " +
            "       (select ur.role_id from sys_user_role ur where ur.user_ID = #{userId})))")
    Set<AuthorityResource> selectAuthorityResourceByUserIdAndResourceId(Long userId);
}