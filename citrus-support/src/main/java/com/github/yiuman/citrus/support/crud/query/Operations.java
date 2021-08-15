package com.github.yiuman.citrus.support.crud.query;

import com.baomidou.mybatisplus.core.toolkit.StringPool;

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
    IN("IN"),

    NOT_IN("NOT IN"),

    LIKE("LIKE"),

    NOT_LIKE("NOT LIKE"),

    EQ("="),

    NE("<>"),

    GT(StringPool.RIGHT_CHEV),

    GE(">="),

    LT(StringPool.LEFT_CHEV),

    LE("<="),

    IS_NULL("IS NULL"),

    IS_NOT_NULL("IS NOT NULL"),

    EXISTS("EXISTS"),

    NOT_EXISTS("NOT EXISTS"),

    BETWEEN("BETWEEN"),

    NOT_BETWEEN("NOT BETWEEN");

    private final String type;

    Operations(String type) {
        this.type = type;
    }
}
