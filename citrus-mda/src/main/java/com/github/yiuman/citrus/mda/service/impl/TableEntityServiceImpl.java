package com.github.yiuman.citrus.mda.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yiuman.citrus.mda.converter.TableRelUtils;
import com.github.yiuman.citrus.mda.converter.impl.HistoricConverter;
import com.github.yiuman.citrus.mda.entity.Column;
import com.github.yiuman.citrus.mda.entity.Table;
import com.github.yiuman.citrus.mda.entity.history.HistoryTable;
import com.github.yiuman.citrus.mda.service.TableEntityService;
import com.github.yiuman.citrus.support.crud.mapper.CrudMapper;
import com.github.yiuman.citrus.support.crud.service.BaseService;
import com.github.yiuman.citrus.support.utils.CrudUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 表实体服务类，维护动态表与DDL操作结合
 *
 * @author yiuman
 * @date 2021/4/20
 */
@Service
public class TableEntityServiceImpl extends BaseService<Table, String> implements TableEntityService {

    private final HistoricConverter historicConverter = new HistoricConverter();

    public TableEntityServiceImpl() {
    }

    private CrudMapper<HistoryTable> getHistoryTableCrudMapper() throws Exception {
        return CrudUtils.getCrudMapper(HistoryTable.class);
    }

    @Override
    public boolean beforeSave(Table entity) throws Exception {
        //实体变更前保存一份历史记录
        if (StringUtils.isBlank(entity.getUuid())) {
            Table table = get(entity.getUuid());
            if (Objects.nonNull(table)) {
                CrudMapper<HistoryTable> historyTableCrudMapper = getHistoryTableCrudMapper();
                HistoryTable historyTable = entity2History(table);
                historyTableCrudMapper.saveEntity(historyTable);
                TableRelUtils.saveTableRelInfos(historyTable.getColumns(), historyTable.getUuid());
                TableRelUtils.saveTableRelInfos(historyTable.getIndexes(), historyTable.getUuid());
            }
        }
        return true;
    }

    @Override
    public void afterSave(Table entity) throws Exception {
        TableRelUtils.saveTableRelInfos(entity.getColumns(), entity.getUuid());
        if (!CollectionUtils.isEmpty(entity.getIndexes())) {
            //索引没给名字默认是列名+"_"
            entity.getIndexes().forEach(indexes -> {
                if (StringUtils.isBlank(indexes.getIndexName())) {
                    indexes.setIndexName(indexes.getColumns().stream().map(Column::getColumnName).collect(Collectors.joining("_")));
                }
            });
            TableRelUtils.saveTableRelInfos(entity.getIndexes(), entity.getUuid());
        }

    }

    @Override
    public HistoryTable entity2History(Table table) {
        return historicConverter.convert(table);
    }
}
