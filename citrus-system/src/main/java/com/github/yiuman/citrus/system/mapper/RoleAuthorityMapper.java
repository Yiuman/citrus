package com.github.yiuman.citrus.system.mapper;

import com.github.yiuman.citrus.support.crud.mapper.CrudMapper;
import com.github.yiuman.citrus.system.entity.RoleAuthority;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author yiuman
 * @date 2020/3/31
 */
@Mapper
@Repository
public interface RoleAuthorityMapper extends CrudMapper<RoleAuthority> {

}