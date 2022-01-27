package com.github.yiuman.citrus.mda.ddl.strategy.impl;

import com.github.yiuman.citrus.mda.ddl.MetadataContext;
import com.github.yiuman.citrus.mda.ddl.strategy.ExecutionStrategy;
import com.github.yiuman.citrus.mda.mapper.DdlMapper;
import com.github.yiuman.citrus.mda.meta.TableMeta;
import org.springframework.util.CollectionUtils;

/**
 * 元数据创表策略
 *
 * @author yiuman
 * @date 2021/4/25
 */
public class TableCreateExecutionStrategy implements ExecutionStrategy<TableMeta> {

    public TableCreateExecutionStrategy() {
    }

    @Override
    public void execute(MetadataContext<TableMeta> metadataContext) {
        TableMeta metadata = metadataContext.getMetadata();
        DdlMapper mapper = getDdlMapper(metadataContext);
        mapper.createTable(metadata);
        afterCreateTable(metadataContext);
    }

    /**
     * 创建完后的操作
     *
     * @param metadataContext 元数据上下文
     */
    private void afterCreateTable(MetadataContext<TableMeta> metadataContext) {
        TableMeta tableMeta = metadataContext.getMetadata();
        if (!CollectionUtils.isEmpty(tableMeta.getIndexes())) {
            DdlMapper mapper = getDdlMapper(metadataContext);
            tableMeta.getIndexes().forEach(indexMeta -> {
                indexMeta.setTable(tableMeta);
                mapper.createIndex(indexMeta);
            });
        }
    }

    private DdlMapper getDdlMapper(MetadataContext<TableMeta> metadataContext) {
        return metadataContext.getSqlSessionFactory().openSession().getMapper(DdlMapper.class);
    }


}
