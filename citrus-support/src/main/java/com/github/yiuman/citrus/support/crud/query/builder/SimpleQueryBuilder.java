package com.github.yiuman.citrus.support.crud.query.builder;

import com.github.yiuman.citrus.support.crud.query.Query;

/**
 * 最简单的QueryBuilder实现
 *
 * @author yiuman
 * @date 2021/8/22
 */
public class SimpleQueryBuilder extends AbstractQueryBuilder<SimpleQueryBuilder> {

    public SimpleQueryBuilder() {
    }

    public SimpleQueryBuilder(Query query) {
        this.query = query;
    }

    @SuppressWarnings("UnusedReturnValue")
    public <T> LambdaQueryBuilder<T> toLambda() {
        return new LambdaQueryBuilder<>(query);
    }
}
