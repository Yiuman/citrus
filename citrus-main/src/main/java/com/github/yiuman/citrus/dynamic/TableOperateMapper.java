package com.github.yiuman.citrus.dynamic;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Repository;

/**
 * @author yiuman
 * @date 2021/3/27
 */
@Mapper
@Repository
public interface TableOperateMapper {

    /**
     * 创表操作
     *
     * @param entity 表信息实体
     */
    @UpdateProvider(type = TableOperation.class)
    void createTable(TableEntity entity);
}