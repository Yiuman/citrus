package com.github.yiuman.citrus.system.mapper;

import com.github.yiuman.citrus.support.crud.EnumArrayHandler;
import com.github.yiuman.citrus.support.crud.mapper.CrudMapper;
import com.github.yiuman.citrus.system.entity.Scope;
import com.github.yiuman.citrus.system.entity.ScopeDefine;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yiuman
 * @date 2020/6/11
 */
@Mapper
@Repository
public interface ScopeDefineMapper extends CrudMapper<ScopeDefine> {

    /**
     * 根据资源ID获取数据范围
     *
     * @param resourceId 资源ID
     * @return 数据范围实体
     */
    @Select("select sc.* from sys_scope sc , sys_auth_scope sa  where sc.scope_Id = sa.scope_Id and sa.resource_Id = #{resourceId}")
    Scope getScopeByResourceId(Long resourceId);


    /**
     * 根据资源ID获取数据范围定义
     *
     * @param resourceId 资源ID
     * @return 数据范围定义集合
     */
    @Select("select sd.* from " +
            "(select sc.* from sys_auth_resource sa , sys_scope sc where sc.scope_Id = sa.scope_Id and sa.resource_Id = #{resourceId}) scope " +
            "left join sys_scope_define sd on scope.scope_id = sd.scope_id order by sd.scope_id")
    @Results({
            @Result(property = "scopeTypes",column = "scope_types",typeHandler = EnumArrayHandler.class)
    })
    List<ScopeDefine> getScopeDefinesByResourceId(Long resourceId);
}