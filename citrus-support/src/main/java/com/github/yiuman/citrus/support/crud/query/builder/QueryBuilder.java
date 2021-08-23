package com.github.yiuman.citrus.support.crud.query.builder;

import com.github.yiuman.citrus.support.crud.query.Query;

/**
 * @author yiuman
 * @date 2021/8/22
 */
public interface QueryBuilder {

    /**
     * 获取查询实例
     *
     * @return 查询实例
     */
    Query toQuery();
}
