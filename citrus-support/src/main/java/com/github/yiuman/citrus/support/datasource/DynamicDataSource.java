package com.github.yiuman.citrus.support.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Optional;

/**
 * 动态数据源
 *
 * @author yiuman
 * @date 2020/11/30
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    public DynamicDataSource() {
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return Optional.ofNullable(DynamicDataSourceHolder.peek()).orElse("");
    }

}
