package com.github.yiuman.citrus.support.crud.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.github.yiuman.citrus.support.model.Tree;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 树形结构Mapper,用于预遍历排序树（左右值）
 *
 * @param <T> 实体类型
 * @author yiuman
 * @date 2020/4/10
 */
public interface TreeMapper<T extends Tree<?>> extends CrudMapper<T> {

    /**
     * 查询左右值的父子链路，如子节点查询祖先链路，或父节点查询子孙链路
     *
     * @param table   表名
     * @param wrapper 查询条件Wrapper
     * @return 树链路集合
     */
    @Select("select distinct t2.* from ${table} t1,${table} t2  ${ew.customSqlSegment}")
    List<T> treeLink(@Param("table") String table, @Param(Constants.WRAPPER) Wrapper<T> wrapper);

}