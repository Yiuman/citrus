package com.github.yiuman.citrus.mda.ddl.strategy.impl;

import com.github.yiuman.citrus.mda.ddl.MetadataContext;
import com.github.yiuman.citrus.mda.ddl.strategy.ExecutionStrategy;
import com.github.yiuman.citrus.mda.mapper.DdlMapper;
import com.github.yiuman.citrus.mda.meta.TableMeta;

/**
 * 表先删侯建策略
 *
 * @author yiuman
 * @date 2021/4/27
 */
public class TableDropCreateExecutionStrategy implements ExecutionStrategy<TableMeta> {

    private final TableCreateExecutionStrategy createExecutionStrategy = new TableCreateExecutionStrategy();

    public TableDropCreateExecutionStrategy() {
    }

    @Override
    public void execute(MetadataContext<TableMeta> metadataContext) {
        DdlMapper mapper = metadataContext.getSqlSessionFactory().openSession().getMapper(DdlMapper.class);
        mapper.dropTable(metadataContext.getMetadata());
        createExecutionStrategy.execute(metadataContext);
    }
}
