package com.github.yiuman.citrus.mda.ddl.analyzer;

import cn.hutool.core.util.ClassUtil;
import com.github.yiuman.citrus.mda.ddl.Action;
import com.github.yiuman.citrus.mda.ddl.MetadataContext;
import com.github.yiuman.citrus.mda.ddl.strategy.ExecutionStrategy;

/**
 * 元数据分析器
 *
 * @param <T> 元数据类型
 * @author yiuman
 * @date 2021/4/25
 */
public interface MetadataAnalyzer<T> {

    /**
     * 分析元数据，得到元数据执行策略
     *
     * @param action  当前事件的类型
     * @param context 元数据信息
     * @return 符合情形的执行策略
     */
    ExecutionStrategy<T> analyzer(Action action, MetadataContext<T> context);

    /**
     * 获取元对象的类型
     *
     * @return 元数据类型
     */
    @SuppressWarnings("unchecked")
    default Class<T> getMetaType() {
        return (Class<T>) ClassUtil.getTypeArgument(this.getClass());
    }
}