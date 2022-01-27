package com.github.yiuman.citrus.mda.converter.impl;

import com.github.yiuman.citrus.mda.converter.Converter;
import com.github.yiuman.citrus.mda.converter.TableRelUtils;
import com.github.yiuman.citrus.mda.entity.Column;
import com.github.yiuman.citrus.mda.entity.Indexes;
import com.github.yiuman.citrus.mda.entity.Table;
import com.github.yiuman.citrus.mda.entity.history.HistoryColumn;
import com.github.yiuman.citrus.mda.entity.history.HistoryIndexes;
import com.github.yiuman.citrus.mda.entity.history.HistoryTable;
import com.github.yiuman.citrus.support.utils.ConvertUtils;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 表实体转换为历史表实体
 *
 * @author yiuman
 * @date 2021/4/29
 */
public class HistoricConverter implements Converter<Table, HistoryTable> {

    public HistoricConverter() {
    }

    @Override
    public HistoryTable convert(Table table) {
        HistoryTable historyTable = Converter.super.convert(table);
        historyTable.setUuid(null);
        historyTable.setTableUuid(table.getUuid());
        List<Column> columnList = TableRelUtils.getTableRelInfos(table.getUuid(), Column.class);
        if (!CollectionUtils.isEmpty(columnList)) {
            historyTable.setColumns(
                    columnList.parallelStream()
                            .map(LambdaUtils.functionWrapper(column -> ConvertUtils.convert(HistoryColumn.class, column)))
                            .collect(Collectors.toList())
            );
        }
        List<Indexes> indexesList = TableRelUtils.getTableRelInfos(table.getUuid(), Indexes.class);
        if (!CollectionUtils.isEmpty(indexesList)) {
            historyTable.setIndexes(
                    indexesList
                            .parallelStream()
                            .map(LambdaUtils.functionWrapper(indexes -> ConvertUtils.convert(HistoryIndexes.class, indexes)))
                            .collect(Collectors.toList())
            );
        }
        return historyTable;
    }
}
