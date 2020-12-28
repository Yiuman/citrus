package com.github.yiuman.citrus.system.mapper;

import com.github.yiuman.citrus.support.crud.mapper.CrudMapper;
import com.github.yiuman.citrus.system.entity.Organization;
import com.github.yiuman.citrus.system.entity.User;
import com.github.yiuman.citrus.system.entity.UserOrgan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用于组织机构Mapper
 *
 * @author yiuman
 * @date 2020/7/24
 */
@Mapper
@Repository
public interface UserOrganMapper extends CrudMapper<UserOrgan> {

    /**
     * 根据用户ID获取组织机构信息
     *
     * @param userId 用户ID
     * @return 组织机构集合
     */
    @Select("select * from sys_organ so, sys_user_organ suo where so.organ_id = suo.organ_id and suo.user_id = #{userId}")
    List<Organization> getOrgansByUserId(Long userId);

    /**
     * 根据组织机构ID获取用户集合
     *
     * @param deptIds 组织机构ID
     * @return 组织机构集合
     */
    @Select("select * from sys_user su, sys_user_organ suo where su.user_id = suo.user_id and suo.organ_id in #{deptIds}")
    List<User> getUsersByDeptIds(List<Long> deptIds);
}