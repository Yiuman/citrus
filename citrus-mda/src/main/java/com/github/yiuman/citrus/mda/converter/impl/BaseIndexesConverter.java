package com.github.yiuman.citrus.mda.converter.impl;

import com.github.yiuman.citrus.mda.converter.TableRelConverter;
import com.github.yiuman.citrus.mda.entity.BaseIndexes;
import com.github.yiuman.citrus.mda.meta.IndexMeta;

/**
 * 基础索引转换器
 *
 * @param <S> 索引实体
 * @author yiuman
 * @date 2021/4/29
 */
public abstract class BaseIndexesConverter<S extends BaseIndexes> implements TableRelConverter<S, IndexMeta> {

    public BaseIndexesConverter() {
    }
}
