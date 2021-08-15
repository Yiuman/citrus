package com.github.yiuman.citrus.support.crud.query;

/**
 * 查询操作类型
 *
 * @author yiuman
 * @date 2021/8/15
 */
public enum Operations {

    /**
     * SQL IN
     */
    IN("in"),

    IN_SQL("inSql"),

    NOT_IN("notIn"),

    LIKE("like"),

    NOT_LIKE("notLike"),

    EQ("eq"),

    NE("ne"),

    GT("gt"),

    GE("ge"),

    LT("lt"),

    LE("le"),

    IS_NULL("isNull"),

    IS_NOT_NULL("isNotNull"),

    EXISTS("exists"),

    NOT_EXISTS("notExists"),

    BETWEEN("between"),

    NOT_BETWEEN("notBetween");

    private final String type;

    Operations(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
