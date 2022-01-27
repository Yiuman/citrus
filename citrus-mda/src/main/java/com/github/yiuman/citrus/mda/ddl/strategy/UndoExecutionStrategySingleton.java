package com.github.yiuman.citrus.mda.ddl.strategy;

import com.github.yiuman.citrus.mda.ddl.MetadataContext;

/**
 * 什么都不做的策略
 *
 * @author yiuman
 * @date 2021/4/27
 */
public final class UndoExecutionStrategySingleton {

    private  static final UndoExecutionStrategy<?> STRATEGY = new UndoExecutionStrategy<>();

    private UndoExecutionStrategySingleton() {
    }

    @SuppressWarnings("unchecked")
    public static <T> UndoExecutionStrategy<T> getUndoStrategy() {
        return (UndoExecutionStrategy<T>) STRATEGY;
    }

    static class UndoExecutionStrategy<T> implements ExecutionStrategy<T> {

        @Override
        public void execute(MetadataContext<T> metadataContext) {

        }
    }
}
