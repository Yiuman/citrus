package com.github.yiuman.citrus.support.crud.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.github.yiuman.citrus.support.model.Tree;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author yiuman
 * @date 2020/4/10
 */
public interface TreeMapper<T extends Tree<?>> extends CrudMapper<T> {

    @Select("select distinct t2.* from ${table} t1,${table} t2  ${ew.customSqlSegment}")
    List<T> list(@Param("table") String table,  @Param(Constants.WRAPPER) Wrapper<T> wrapper);

}