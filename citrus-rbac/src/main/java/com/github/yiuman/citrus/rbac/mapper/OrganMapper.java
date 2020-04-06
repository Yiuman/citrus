package com.github.yiuman.citrus.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yiuman.citrus.rbac.entity.Organization;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yiuman
 * @date 2020/4/6
 */
@Mapper
public interface OrganMapper extends BaseMapper<Organization> {
}