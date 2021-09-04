package com.github.yiuman.citrus.support.crud.query.builder;


import com.github.yiuman.citrus.support.crud.query.Fn;
import com.github.yiuman.citrus.support.crud.query.Query;
import com.github.yiuman.citrus.support.utils.LambdaUtils;

import java.util.Collection;

/**
 * lambda方式的查询拼接
 *
 * @param <T> 实体类型
 * @author yiuman
 * @date 2021/8/20
 */
public class LambdaQueryBuilder<T> extends AbstractQueryBuilder<LambdaQueryBuilder<T>> {

    public LambdaQueryBuilder() {
    }

    public LambdaQueryBuilder(Query query) {
        this.query = query;
    }

    public LambdaQueryBuilder<T> eq(Fn<T, ?> fn, Object value) {
        eq(LambdaUtils.getPropertyName(fn), value);
        return this;
    }

    public LambdaQueryBuilder<T> in(Fn<T, ?> fn, Collection<?> values) {
        in(LambdaUtils.getPropertyName(fn), values);
        return this;
    }
}
