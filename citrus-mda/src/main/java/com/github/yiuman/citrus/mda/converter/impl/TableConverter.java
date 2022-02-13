package com.github.yiuman.citrus.mda.converter.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.yiuman.citrus.mda.converter.Converter;
import com.github.yiuman.citrus.mda.converter.TableRelConverter;
import com.github.yiuman.citrus.mda.entity.BaseIndexes;
import com.github.yiuman.citrus.mda.entity.BaseTable;
import com.github.yiuman.citrus.mda.entity.Table;
import com.github.yiuman.citrus.mda.exception.ConverterException;
import com.github.yiuman.citrus.mda.meta.*;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 基础的表格转换器 （实体表->数据库元信息表）
 * 表转换器（实体表->元数据表）
 *
 * @param <S> 表对象类型
 * @author yiuman
 * @date 2021/4/29
 */
public class TableConverter<S extends BaseTable> implements Converter<S, TableMeta> {

    private final ColumnConverter columnConverter = new ColumnConverter();

    private TableRelConverter<? extends BaseIndexes, IndexMeta> indexesConverter;

    public TableConverter() {
    }

    public TableRelConverter<? extends BaseIndexes, IndexMeta> getIndexesConverter() {
        if (Objects.isNull(indexesConverter)) {
            indexesConverter = getSourceClass().isAssignableFrom(Table.class)
                    ? new IndexesConverter()
                    : new HistoryIndexesConverter();
        }

        return indexesConverter;
    }

    @Override
    public TableMeta convert(S table) {
        TableMeta tableMeta = TableMeta.builder()
                .namespace(table.getNamespace())
                .tableName(table.getTableName())
                .comments(table.getComments()).build();

        List<ColumnMeta> metaColumns = columnConverter.convertList(table.getUuid());
        List<IndexMeta> metaIndexes = getIndexesConverter().convertList(table.getUuid());
        final List<Constraint> constraints = CollectionUtil.newArrayList();
        //添加主键约束
        constraints.addAll(getColumnsConstraints(metaColumns, ColumnMeta::getPrimaryKey, PrimaryKeyConstraint.class));
        //添加唯一约束
        constraints.addAll(getColumnsConstraints(metaColumns, ColumnMeta::getUniques, UniqueConstraint.class));

        metaColumns.parallelStream().forEach(columnMeta -> columnMeta.setTable(tableMeta));
        metaIndexes.parallelStream().forEach(indexMeta -> indexMeta.setTable(tableMeta));
        tableMeta.setColumns(metaColumns);
        tableMeta.setConstraints(constraints);
        tableMeta.setIndexes(metaIndexes);
        return tableMeta;
    }

    /**
     * 根据元字段信息获取预约值（唯一预约、主键约束、外键约束等）
     *
     * @param metaColumns     元字段信息
     * @param columnPredicate 断言
     * @param clazz           具体的约束类型
     * @param <T>             具体的约束类型泛型
     * @return 子弹约束的集合
     * @throws ConverterException 转换异常
     */
    private <T extends BaseColumnConstraint<T>> List<Constraint> getColumnsConstraints(
            List<ColumnMeta> metaColumns, Predicate<ColumnMeta> columnPredicate, Class<T> clazz
    ) throws ConverterException {
        try {
            final List<Constraint> constraints = CollectionUtil.newArrayList();
            List<ColumnMeta> matchColumns = metaColumns.stream().filter(columnPredicate).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(matchColumns)) {
                constraints.add(clazz.getConstructor().newInstance().addColumn(matchColumns.toArray(new ColumnMeta[]{})));
            }
            return constraints;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            throw new ConverterException(ex);
        }

    }

}
