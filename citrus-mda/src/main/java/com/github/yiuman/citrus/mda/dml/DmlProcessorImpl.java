package com.github.yiuman.citrus.mda.dml;

import com.github.yiuman.citrus.support.datasource.DynamicDataSourceHolder;
import com.github.yiuman.citrus.support.datasource.DynamicSqlSessionTemplate;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * @author yiuman
 * @date 2021/4/29
 */
public class DmlProcessorImpl implements DmlProcessor {

    private final DynamicSqlSessionTemplate dynamicSqlSessionTemplate;

    public DmlProcessorImpl(DynamicSqlSessionTemplate dynamicSqlSessionTemplate) {
        this.dynamicSqlSessionTemplate = dynamicSqlSessionTemplate;
    }

    @Override
    public SqlSessionFactory getSqlSessionFactory(String namespace) {
        DynamicDataSourceHolder.push(namespace);
        return dynamicSqlSessionTemplate.getSqlSessionFactory();
    }
}
