package com.github.yiuman.citrus.support.crud.query.builder;

import com.github.yiuman.citrus.support.crud.query.Query;

import java.util.Optional;

/**
 * @author yiuman
 * @date 2021/8/22
 */
public final class QueryBuilders {

    private QueryBuilders() {
    }

    public static SimpleQueryBuilder create() {
        return new SimpleQueryBuilder();
    }

    public static  SimpleQueryBuilder wrapper(Query query){
        return  new SimpleQueryBuilder(Optional.ofNullable(query).orElse(Query.create()));
    }

    public static <T> LambdaQueryBuilder<T> lambda() {
        return new LambdaQueryBuilder<>();
    }

    public static Query empty() {
        return Query.create();
    }
}
