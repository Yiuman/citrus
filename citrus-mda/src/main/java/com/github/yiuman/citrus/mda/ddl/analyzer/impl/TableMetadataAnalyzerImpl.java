package com.github.yiuman.citrus.mda.ddl.analyzer.impl;

import com.github.yiuman.citrus.mda.ddl.Action;
import com.github.yiuman.citrus.mda.ddl.MetadataContext;
import com.github.yiuman.citrus.mda.ddl.analyzer.TableMetadataAnalyzer;
import com.github.yiuman.citrus.mda.ddl.strategy.ExecutionStrategy;
import com.github.yiuman.citrus.mda.ddl.strategy.UndoExecutionStrategySingleton;
import com.github.yiuman.citrus.mda.ddl.strategy.impl.TableBackupCreateExecutionStrategy;
import com.github.yiuman.citrus.mda.ddl.strategy.impl.TableBackupExecutionStrategy;
import com.github.yiuman.citrus.mda.ddl.strategy.impl.TableCreateExecutionStrategy;
import com.github.yiuman.citrus.mda.ddl.strategy.impl.TableDropCreateExecutionStrategy;
import com.github.yiuman.citrus.mda.mapper.DdlMapper;
import com.github.yiuman.citrus.mda.meta.TableMeta;

/**
 * 表数据源分析器
 *
 * @author yiuman
 * @date 2021/4/25
 */
public class TableMetadataAnalyzerImpl implements TableMetadataAnalyzer {

    public TableMetadataAnalyzerImpl() {
    }

    @Override
    public ExecutionStrategy<TableMeta> analyzer(Action action, MetadataContext<TableMeta> metadataContext) {
        switch (action) {
            case CREATE:
                TableMeta tableMeta = metadataContext.getMetadata();
                DdlMapper mapper = metadataContext.getSqlSessionFactory().openSession().getMapper(DdlMapper.class);
                if (!mapper.exist(tableMeta.getTableName(), tableMeta.getNamespace())) {
                    return new TableCreateExecutionStrategy();
                } else {
                    return tableUpdateExecutionStrategy(metadataContext);
                }
            case UPDATE:
                return tableUpdateExecutionStrategy(metadataContext);
            case DELETE:
                return tableDeleteExecutionStrategy(metadataContext);
            default:
                return UndoExecutionStrategySingleton.getUndoStrategy();
        }

    }

    /**
     * 表变更策略
     *
     * @param metadataContext 元数据上下文（这里是表的上下文）
     * @return 执行策略
     */
    private ExecutionStrategy<TableMeta> tableUpdateExecutionStrategy(MetadataContext<TableMeta> metadataContext) {
        DdlMapper mapper = metadataContext.getSqlSessionFactory().openSession().getMapper(DdlMapper.class);
        TableMeta tableMeta = metadataContext.getMetadata();
        Integer tableRows = mapper.getTableRows(tableMeta.getTableName(), tableMeta.getNamespace());
        //2.若有数据先进行备份再进行创建，否则删掉后重建
        if (tableRows <= 0) {
            return new TableDropCreateExecutionStrategy();
        } else {
            return new TableBackupCreateExecutionStrategy();
        }
    }

    private ExecutionStrategy<TableMeta> tableDeleteExecutionStrategy(MetadataContext<TableMeta> metadataContext) {
        DdlMapper mapper = metadataContext.getSqlSessionFactory().openSession().getMapper(DdlMapper.class);
        TableMeta tableMeta = metadataContext.getMetadata();
        if (!mapper.exist(tableMeta.getTableName(), tableMeta.getNamespace())
                || mapper.getTableRows(tableMeta.getTableName(), tableMeta.getNamespace()) <= 0) {
            return (context) -> mapper.dropTable(context.getMetadata());
        } else {
            return new TableBackupExecutionStrategy();
        }

    }
}
