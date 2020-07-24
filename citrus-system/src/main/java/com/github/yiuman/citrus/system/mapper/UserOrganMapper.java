package com.github.yiuman.citrus.system.mapper;

import com.github.yiuman.citrus.support.crud.mapper.CrudMapper;
import com.github.yiuman.citrus.system.entity.Organization;
import com.github.yiuman.citrus.system.entity.UserOrgan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用于组织机构Mapper
 *
 * @author yiuman
 * @date 2020/7/24
 */
@Mapper
public interface UserOrganMapper extends CrudMapper<UserOrgan> {

    @Select("select * from sys_organ so, sys_user_organ suo where so.organ_id = suo.organ_id and suo.user_id = #{userId}")
    List<Organization> getOrgansByUserId(Long userId);
}