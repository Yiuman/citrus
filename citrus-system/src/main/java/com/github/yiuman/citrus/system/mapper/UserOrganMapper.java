package com.github.yiuman.citrus.system.mapper;

import com.github.yiuman.citrus.support.crud.mapper.CrudMapper;
import com.github.yiuman.citrus.system.entity.UserOrgan;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yiuman
 * @date 2020/4/11
 */
@Mapper
public interface UserOrganMapper extends CrudMapper<UserOrgan> {

}
