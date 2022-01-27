package com.github.yiuman.citrus.mda.ddl.strategy;

import com.github.yiuman.citrus.mda.ddl.MetadataContext;

/**
 * 元数据执行策略
 *
 * @param <T> 元数据实体
 * @author yiuman
 * @date 2021/4/25
 */
public interface ExecutionStrategy<T> {

    /**
     * 元数据上下文
     *
     * @param metadataContext 上下文信息
     */
    void execute(MetadataContext<T> metadataContext);
}