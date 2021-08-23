package com.github.yiuman.citrus.support.crud.query.builder;

import com.github.yiuman.citrus.support.crud.query.Operations;
import com.github.yiuman.citrus.support.crud.query.Query;
import com.github.yiuman.citrus.support.model.SortBy;

import java.util.Collection;

/**
 * @author yiuman
 * @date 2021/8/22
 */
@SuppressWarnings({"unchecked", "UnusedReturnValue", "unused", "RedundantSuppression"})
public abstract class AbstractQueryBuilder<Children extends AbstractQueryBuilder<Children>> implements QueryBuilder {

    protected Query query = Query.create();

    public AbstractQueryBuilder() {
    }

    public Children eq(String name, Object value) {
        query.addConditionInfo(name, value, Operations.EQ);
        return (Children) this;
    }

    public Children in(String parameter, Collection<?> values) {
        query.addConditionInfo(parameter, values, Operations.IN);
        return (Children) this;
    }

    public Children like(String parameter, Object value) {
        query.addConditionInfo(parameter, value, Operations.LIKE);
        return (Children) this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Children inSql(String parameter, String value) {
        query.addConditionInfo(parameter, value, Operations.IN_SQL);
        return (Children) this;
    }

    public Children orderBy(String parameter, boolean desc) {
        query.getSorts().add(new SortBy(parameter, desc));
        return (Children) this;
    }

    public Children orderBy(SortBy sortBy) {
        query.getSorts().add(sortBy);
        return (Children) this;
    }

    @Override
    public Query toQuery() {
        return this.query;
    }
}
