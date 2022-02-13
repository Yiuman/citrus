package com.github.yiuman.citrus.mda.meta;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 列约束
 *
 * @param <T> 列约束类型
 * @author yiuman
 * @date 2021/4/20
 */
public abstract class BaseColumnConstraint<T extends BaseColumnConstraint<T>> implements Constraint {

    private String constraintName;

    protected final LinkedHashSet<ColumnMeta> columns = new LinkedHashSet<>();

    public BaseColumnConstraint() {
    }

    @Override
    public String getConstraintName() {
        return Optional.ofNullable(constraintName).orElse(columns.stream().map(ColumnMeta::getColumnName).collect(Collectors.joining("_")));
    }

    public void setConstraintName(String constraintName) {
        this.constraintName = constraintName;
    }

    @Override
    public LinkedHashSet<ColumnMeta> getColumns() {
        return columns;
    }

    @SuppressWarnings("unchecked")
    public T addColumn(ColumnMeta... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return (T) this;
    }

}
