package com.github.yiuman.citrus.mda.ddl.strategy;

import com.github.yiuman.citrus.mda.ddl.MetadataContext;

import java.util.ArrayDeque;

/**
 * 执行策略队列，用于执行多事件，比如创建表的时候，可能要在后边创建索引
 *
 * @param <T> 元数据实体
 * @author yiuman
 * @date 2021/4/27
 */
public class ExecutionStrategyDeque<T> extends ArrayDeque<ExecutionStrategy<T>> implements ExecutionStrategy<T> {

    public ExecutionStrategyDeque() {
    }

    @Override
    public void execute(MetadataContext<T> metadataContext) {
        this.forEach(deque -> deque.execute(metadataContext));
    }
}
