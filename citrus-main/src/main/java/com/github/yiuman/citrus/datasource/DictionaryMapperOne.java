package com.github.yiuman.citrus.datasource;

import com.github.yiuman.citrus.support.crud.mapper.CrudMapper;
import com.github.yiuman.citrus.support.datasource.DataSource;
import com.github.yiuman.citrus.system.entity.Dictionary;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author yiuman
 * @date 2020/12/2
 */
@Repository
@Mapper
@DataSource
public interface DictionaryMapperOne extends CrudMapper<Dictionary> {
}