package com.github.yiuman.citrus.mda.ddl.strategy.impl;

import com.github.yiuman.citrus.mda.ddl.MetadataContext;
import com.github.yiuman.citrus.mda.ddl.strategy.ExecutionStrategy;
import com.github.yiuman.citrus.mda.mapper.DdlMapper;
import com.github.yiuman.citrus.mda.meta.TableMeta;

/**
 * 数据表备份（这里备份只做重命名操作）
 *
 * @author yiuman
 * @date 2021/4/27
 */
public class TableBackupExecutionStrategy implements ExecutionStrategy<TableMeta> {

    public TableBackupExecutionStrategy() {
    }

    @Override
    public void execute(MetadataContext<TableMeta> metadataContext) {
        DdlMapper mapper = metadataContext.getSqlSessionFactory().openSession().getMapper(DdlMapper.class);
        TableMeta metadata = metadataContext.getMetadata();
        mapper.rename(metadata.getTableName(), metadata.getNamespace(), metadata.getTableName() + "_backup");
    }
}
