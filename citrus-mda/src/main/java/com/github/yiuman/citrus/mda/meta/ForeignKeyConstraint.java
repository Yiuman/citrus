package com.github.yiuman.citrus.mda.meta;

/**
 * 外键约束
 *
 * @author yiuman
 * @date 2021/4/20
 */
public class ForeignKeyConstraint extends BaseColumnConstraint<ForeignKeyConstraint> {

    public ForeignKeyConstraint() {
    }

    @Override
    public String getTypeName() {
        return FOREIGN;
    }
}
