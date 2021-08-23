package com.github.yiuman.citrus.support.crud.query;

import com.github.yiuman.citrus.support.model.SortBy;

import java.util.ArrayList;
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

    public static Query create() {
        return new Query();
    }

    public void addConditionInfo(String parameter, Object value, Operations operations) {
        conditions.add(ConditionInfo.builder()
                .operator(operations.getType())
                .parameter(parameter)
                .mapping(parameter)
                .value(value)
                .type(value.getClass())
                .build());
    }
}
