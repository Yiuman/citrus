package com.github.yiuman.citrus.support.crud.query;

import com.github.yiuman.citrus.support.model.SortBy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 查询对象
 *
 * @author yiuman
 * @date 2021/8/15
 */
@SuppressWarnings({"unused", "RedundantSuppression"})
public class Query {

    private List<ConditionInfo> conditions = new ArrayList<>();

    private List<SortBy> sorts = new ArrayList<>();

    public Query() {
    }

    public List<ConditionInfo> getConditions() {
        return conditions;
    }

    public void setConditions(List<ConditionInfo> conditions) {
        this.conditions = conditions;
    }

    public List<SortBy> getSorts() {
        return sorts;
    }

    public void setSorts(List<SortBy> sorts) {
        this.sorts = sorts;
    }

    public static Query of() {
        return new Query();
    }

    public Query add(ConditionInfo conditionInfo) {
        conditions.add(conditionInfo);
        return this;
    }

    public Query eq(String parameter, Object val) {
        conditions.add(ConditionInfo.builder()
                .operator("eq")
                .parameter(parameter)
                .mapping(parameter)
                .value(val)
                .type(val.getClass())
                .build());
        return this;
    }

    public Query in(String parameter, Collection<?> parentIds) {
        conditions.add(ConditionInfo.builder()
                .operator("in")
                .parameter(parameter)
                .mapping(parameter)
                .value(parentIds)
                .type(parentIds.getClass())
                .build());
        return this;
    }

    public Query like(String parameter, Object value) {
        conditions.add(ConditionInfo.builder()
                .operator("like")
                .parameter(parameter)
                .mapping(parameter)
                .value(value)
                .type(value.getClass())
                .build());
        return this;
    }

    public Query express(String parameter, String express) {
        conditions.add(ConditionInfo.builder()
                .operator(express)
                .value(express)
                .parameter(parameter)
                .mapping(parameter)
                .build());
        return this;
    }

    public Query orderBy(String parameter, boolean desc) {
        sorts.add(new SortBy(parameter, desc));
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Query orderBy(SortBy sortBy) {
        sorts.add(sortBy);
        return this;
    }
}
