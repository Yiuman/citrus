package com.github.yiuman.citrus.mda.ddl.analyzer.impl;

import com.github.yiuman.citrus.mda.ddl.Action;
import com.github.yiuman.citrus.mda.ddl.MetadataContext;
import com.github.yiuman.citrus.mda.ddl.analyzer.IndexesMetadataAnalyzer;
import com.github.yiuman.citrus.mda.ddl.strategy.ExecutionStrategy;
import com.github.yiuman.citrus.mda.mapper.DdlMapper;
import com.github.yiuman.citrus.mda.meta.IndexMeta;

import java.util.HashMap;
import java.util.Map;

/**
 * 索引分析器
 *
 * @author yiuman
 * @date 2021/4/27
 */
public class IndexesMetadataAnalyzerImpl implements IndexesMetadataAnalyzer {

    /**
     * 创建策略（简单创建）
     */
    private static final ExecutionStrategy<IndexMeta> CREATE_EXECUTION =
            (context) -> context
                    .getSqlSessionFactory()
                    .openSession()
                    .getMapper(DdlMapper.class)
                    .createIndex(context.getMetadata());

    /**
     * 删除策略（简单删除）
     */
    private static final ExecutionStrategy<IndexMeta> DELETE_EXECUTION =
            (context) -> context
                    .getSqlSessionFactory()
                    .openSession()
                    .getMapper(DdlMapper.class)
                    .dropIndex(context.getMetadata());

    private static final Map<Action, ExecutionStrategy<IndexMeta>> ACTION_EXECUTION_STRATEGY_MAP
            = new HashMap<Action, ExecutionStrategy<IndexMeta>>(3) {{
        put(Action.CREATE, CREATE_EXECUTION);
        put(Action.UPDATE, (context) -> {
            DELETE_EXECUTION.execute(context);
            CREATE_EXECUTION.execute(context);
        });
        put(Action.DELETE, DELETE_EXECUTION);
    }};

    public IndexesMetadataAnalyzerImpl() {
    }

    @Override
    public ExecutionStrategy<IndexMeta> analyzer(Action action, MetadataContext<IndexMeta> context) {
        return ACTION_EXECUTION_STRATEGY_MAP.get(action);
    }
}
