package com.github.yiuman.citrus.mda.meta;

import java.util.LinkedHashSet;

/**
 * 约束
 *
 * @author yiuman
 * @date 2021/4/19
 */
public interface Constraint {

    /**
     * 主键约束
     */
    String PRIMARY = "primary";
    /**
     * 外键约束
     */
    String FOREIGN = "foreign";
    /**
     * 唯一约束
     */
    String UNIQUE = "unique";

    /**
     * 约束名字
     *
     * @return 获取约束名字
     */
    String getConstraintName();

    /**
     * 约束的字段
     *
     * @return 获取约束的字段
     */
    LinkedHashSet<ColumnMeta> getColumns();

    /**
     * 约束类型
     *
     * @return 获取约束的类型
     */
    String getTypeName();

}
