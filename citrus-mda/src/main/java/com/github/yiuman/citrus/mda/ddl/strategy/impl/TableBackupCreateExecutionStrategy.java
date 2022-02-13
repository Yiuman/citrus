package com.github.yiuman.citrus.mda.ddl.strategy.impl;

import com.github.yiuman.citrus.mda.ddl.MetadataContext;
import com.github.yiuman.citrus.mda.ddl.strategy.ExecutionStrategy;
import com.github.yiuman.citrus.mda.ddl.strategy.ExecutionStrategyDeque;
import com.github.yiuman.citrus.mda.meta.TableMeta;

/**
 * 数据表先备份后创建再进行数据同步的创表策略
 *
 * @author yiuman
 * @date 2021/4/27
 */
public class TableBackupCreateExecutionStrategy implements ExecutionStrategy<TableMeta> {

    public TableBackupCreateExecutionStrategy() {
    }

    @Override
    public void execute(MetadataContext<TableMeta> metadataContext) {
        ExecutionStrategyDeque<TableMeta> executionStrategies = new ExecutionStrategyDeque<>();
        executionStrategies.add(new TableBackupExecutionStrategy());
        executionStrategies.add(new TableCreateExecutionStrategy());
        executionStrategies.execute(metadataContext);
    }
}
