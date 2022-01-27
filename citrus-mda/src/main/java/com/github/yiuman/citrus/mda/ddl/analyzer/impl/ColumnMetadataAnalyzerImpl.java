package com.github.yiuman.citrus.mda.ddl.analyzer.impl;

import com.github.yiuman.citrus.mda.ddl.Action;
import com.github.yiuman.citrus.mda.ddl.MetadataContext;
import com.github.yiuman.citrus.mda.ddl.analyzer.ColumnMetadataAnalyzer;
import com.github.yiuman.citrus.mda.ddl.strategy.ExecutionStrategy;
import com.github.yiuman.citrus.mda.mapper.DdlMapper;
import com.github.yiuman.citrus.mda.meta.ColumnMeta;

import java.util.HashMap;
import java.util.Map;

/**
 * 元数据列的分析器
 *
 * @author yiuman
 * @date 2021/4/27
 */
public class ColumnMetadataAnalyzerImpl implements ColumnMetadataAnalyzer {

    private static final Map<Action, ExecutionStrategy<ColumnMeta>>
            ACTION_EXECUTION_STRATEGY_MAP = new HashMap<Action, ExecutionStrategy<ColumnMeta>>(3) {{
        put(Action.CREATE, (context) -> context.getSqlSessionFactory().openSession().getMapper(DdlMapper.class).createColumn(context.getMetadata()));
        put(Action.UPDATE, (context) -> context.getSqlSessionFactory().openSession().getMapper(DdlMapper.class).updateColumn(context.getMetadata()));
        put(Action.DELETE, (context) -> context.getSqlSessionFactory().openSession().getMapper(DdlMapper.class).deleteColumn(context.getMetadata()));
    }};

    public ColumnMetadataAnalyzerImpl() {
    }

    @Override
    public ExecutionStrategy<ColumnMeta> analyzer(Action action, MetadataContext<ColumnMeta> metadata) {
        return ACTION_EXECUTION_STRATEGY_MAP.get(action);
    }
}
