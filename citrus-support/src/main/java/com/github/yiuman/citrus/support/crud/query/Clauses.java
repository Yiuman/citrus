package com.github.yiuman.citrus.support.crud.query;

/**
 * 查询子句
 *
 * @author yiuman
 * @date 2021/8/16
 */
public enum Clauses {

    AND("and"),

    OR("or");

    private final String name;

    Clauses(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
