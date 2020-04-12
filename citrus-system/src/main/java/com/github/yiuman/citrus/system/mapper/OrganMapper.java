package com.github.yiuman.citrus.system.mapper;

import com.github.yiuman.citrus.support.crud.mapper.TreeMapper;
import com.github.yiuman.citrus.system.entity.Organization;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yiuman
 * @date 2020/4/6
 */
@Mapper
public interface OrganMapper extends TreeMapper<Organization> {


}