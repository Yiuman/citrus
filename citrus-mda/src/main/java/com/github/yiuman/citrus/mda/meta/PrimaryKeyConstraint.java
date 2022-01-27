package com.github.yiuman.citrus.mda.meta;

/**
 * 唯一约束
 *
 * @author yiuman
 * @date 2021/4/20
 */
public class PrimaryKeyConstraint extends BaseColumnConstraint<PrimaryKeyConstraint> {

    public PrimaryKeyConstraint() {
    }

    @Override
    public String getTypeName() {
        return PRIMARY;
    }

}
