package com.github.yiuman.citrus.mda.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.mda.converter.impl.TableConverter;
import com.github.yiuman.citrus.mda.ddl.DdlProcessor;
import com.github.yiuman.citrus.mda.dml.DmlProcessor;
import com.github.yiuman.citrus.mda.entity.Table;
import com.github.yiuman.citrus.mda.exception.MdaException;
import com.github.yiuman.citrus.mda.meta.*;
import com.github.yiuman.citrus.mda.service.MdaService;
import com.github.yiuman.citrus.mda.service.TableEntityService;
import com.github.yiuman.citrus.support.crud.query.Query;
import com.github.yiuman.citrus.support.crud.query.QueryHelper;
import com.github.yiuman.citrus.support.crud.query.builder.QueryBuilders;
import com.github.yiuman.citrus.support.crud.query.builder.SimpleQueryBuilder;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import com.github.yiuman.citrus.support.utils.WebUtils;
import org.springframework.stereotype.Service;

import java.sql.JDBCType;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 模型驱动逻辑实现
 *
 * @author yiuman
 * @date 2021/5/5
 */
@Service
public class MdaServiceImpl implements MdaService {

    private final DdlProcessor ddlProcessor;

    private final DmlProcessor dmlProcessor;

    private final TableEntityService tableEntityService;

    private final TableConverter<Table> tableMetaConverter = new TableConverter<>();

    public MdaServiceImpl(DdlProcessor ddlProcessor, DmlProcessor dmlProcessor, TableEntityService tableEntityService) {
        this.ddlProcessor = ddlProcessor;
        this.dmlProcessor = dmlProcessor;
        this.tableEntityService = tableEntityService;
    }

    @Override
    public DdlProcessor getDdlProcessor() {
        return ddlProcessor;
    }

    @Override
    public DmlProcessor getDmlProcessor() {
        return dmlProcessor;
    }

    @Override
    public TableEntityService getTableEntityService() {
        return tableEntityService;
    }

    @Override
    public Table getTableEntity(String tableId) {
        return tableEntityService.get(tableId);
    }

    @Override
    public void createTable(String tableId) {
        getDdlProcessor().createTable(entity2Meta(tableId));
    }

    @Override
    public TableMeta entity2Meta(String uuid) {
        return entity2Meta(
                Optional.ofNullable(tableEntityService.get(uuid))
                        .orElseThrow(() -> new MdaException(String.format("cannot found model entity by [%s]", uuid)))
        );
    }

    @Override
    public TableMeta entity2Meta(Table table) {
        return tableMetaConverter.convert(table);
    }

    @Override
    public String save(Map<String, Object> entity) throws Exception {
        if (!this.beforeSave(entity)) {
            return null;
        }
        Table tableEntity = getTableEntity(getModelId(WebUtils.getRequest()));

        TableMeta tableMeta = entity2Meta(tableEntity);
        PrimaryKeyConstraint primaryKeyConstraint = tableMeta.getPrimaryKey();
        final Map<ColumnMeta, Object> keyMap = new HashMap<>(tableMeta.getColumns().size());
        if (Objects.nonNull(primaryKeyConstraint)) {
            //如果是组合主键必须主动赋值，否则抛异常
            if (isCombinePrimaryKeys(primaryKeyConstraint)) {
                primaryKeyConstraint.getColumns().forEach(primaryKeyColumn -> {
                    Object key = entity.get(primaryKeyColumn.getColumnName());
                    if (Objects.isNull(key)) {
                        //当前模型的主键为组合模式，不能为空
                        throw new MdaException(String.format("The primary key of the current model is in combination mode and cannot be empty."
                                + " The corresponding columns are [%s]", primaryKeyConstraint
                                .getColumns()
                                .stream()
                                .map(ColumnMeta::getColumnName)
                                .collect(Collectors.joining(","))));
                    }
                    keyMap.put(primaryKeyColumn, key);
                });
            } else {
                primaryKeyConstraint.getColumns().stream().findFirst().ifPresent(primaryKeyColumn -> {
                    if (Objects.isNull(entity.get(primaryKeyColumn.getColumnName()))) {
                        final IdentifierGenerator identifierGenerator = GlobalConfigUtils.getGlobalConfig(
                                getDmlProcessor().getSqlSessionFactory(tableEntity.getNamespace()).getConfiguration()).getIdentifierGenerator();
                        IdType idType = tableEntity.getIdType();
                        Object key;
                        if (idType == IdType.ASSIGN_ID) {
                            Number number = identifierGenerator.nextId(entity);
                            key = primaryKeyColumn.getJdbcType().equals(JDBCType.VARCHAR)
                                    ? number.toString()
                                    : number;
                        } else {
                            key = identifierGenerator.nextUUID(entity);
                        }
                        keyMap.put(primaryKeyColumn, key);
                        entity.put(primaryKeyColumn.getColumnName(), key);
                    } else {
                        keyMap.put(primaryKeyColumn, entity.get(primaryKeyColumn.getColumnName()));
                    }

                });
            }
        }

        SaveMeta saveMeta = SaveMeta.builder()
                .namespace(tableMeta.getNamespace())
                .tableName(tableMeta.getTableName())
                .entity(entity).build();

        SimpleQueryBuilder simpleQueryBuilder = QueryBuilders.create();
        keyMap.forEach((key, value) -> simpleQueryBuilder.eq(key.getColumnName(), value));
        if (Objects.isNull(get(simpleQueryBuilder.toQuery()))) {
            getDmlProcessor().insert(saveMeta);
        } else {
            UpdateWrapper<?> updateWrapper = Wrappers.update();
            keyMap.forEach((key, value) -> {
                updateWrapper.eq(key.getColumnName(), value);
                entity.remove(key.getColumnName());
            });
            getDmlProcessor().update(saveMeta, updateWrapper);
        }
        afterSave(entity);
        return keyMap.values().stream().map(Object::toString).collect(Collectors.joining("-"));
    }

    @Override
    public String getKeyProperty() {
        return getKeyColumn();
    }

    @Override
    public String getKeyColumn() {
        Table tableEntity = getTableEntity(getModelId(WebUtils.getRequest()));
        TableMeta tableMeta = entity2Meta(tableEntity);
        return getKeyColumn(tableMeta);
    }

    @Override
    public boolean batchSave(Iterable<Map<String, Object>> entityIterable) {
        entityIterable.forEach(LambdaUtils.consumerWrapper(this::save));
        return true;
    }

    @Override
    public boolean remove(Map<String, Object> entity) {
        TableMeta tableMeta = getTableMeta();
        PrimaryKeyConstraint primaryKey = tableMeta.getPrimaryKey();
        QueryWrapper<?> query = Wrappers.query();
        if (Objects.nonNull(primaryKey)) {
            primaryKey.getColumns()
                    .forEach(keyColumn -> query.eq(keyColumn.getColumnName(), entity.get(keyColumn.getColumnName())));
        }

        return getDmlProcessor()
                .remove(DeleteMeta.builder().namespace(tableMeta.getNamespace()).tableName(tableMeta.getTableName()).build(), query);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void batchRemove(Iterable<String> keys) {
        TableMeta tableMeta = getTableMeta();
        PrimaryKeyConstraint primaryKeyConstraint = tableMeta.getPrimaryKey();
        if (Objects.nonNull(primaryKeyConstraint)) {
            primaryKeyConstraint
                    .getColumns()
                    .stream()
                    .findFirst()
                    .ifPresent(primaryKey -> keys.forEach(
                            key -> remove((Map<String, Object>) QueryHelper.getQueryWrapper(QueryBuilders.create().eq(primaryKey.getColumnName(), key).toQuery())))
                    );
        }

    }

    @Override
    public boolean remove(Query query) {
        TableMeta tableMeta = getTableMeta();
        return getDmlProcessor().remove(DeleteMeta.builder()
                .namespace(tableMeta.getNamespace())
                .tableName(tableMeta.getTableName())
                .build(), QueryHelper.getQueryWrapper(query));
    }

    @Override
    public void clear() {
        remove(QueryBuilders.empty());
    }

    @Override
    public Map<String, Object> get(String key) {
        TableMeta tableMeta = getTableMeta();
        PrimaryKeyConstraint primaryKeyConstraint = tableMeta.getPrimaryKey();

        final AtomicReference<Map<String, Object>> entity = new AtomicReference<>();
        primaryKeyConstraint
                .getColumns()
                .stream()
                .findFirst()
                .ifPresent(primaryKey -> entity.set(
                        get(QueryBuilders.lambda().eq(primaryKey.getColumnName(), key).toQuery())
                ));
        return entity.get();
    }

    @Override
    public Map<String, Object> get(Query query) {
        List<Map<String, Object>> list = list(query);
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        return list.get(0);

    }

    @Override
    public List<Map<String, Object>> list() {
        return list(QueryBuilders.empty());
    }

    @Override
    public List<Map<String, Object>> list(Query query) {
        return getDmlProcessor().list(getTableMeta(), QueryHelper.getQueryWrapper(query));
    }

    @Override
    public <P extends IPage<Map<String, Object>>> P page(P page, Query query) {
        return getDmlProcessor().page(page, getTableMeta(), QueryHelper.getQueryWrapper(query));
    }

    protected String getKeyColumn(TableMeta tableMeta) {
        PrimaryKeyConstraint primaryKeyConstraint = tableMeta.getPrimaryKey();
        return Objects.nonNull(primaryKeyConstraint) ? primaryKeyConstraint
                .getColumns().stream().map(ColumnMeta::getColumnName).collect(Collectors.joining("-")) : null;
    }

    protected boolean isCombinePrimaryKeys(PrimaryKeyConstraint primaryKeyConstraint) {
        return CollectionUtil.isNotEmpty(primaryKeyConstraint.getColumns()) && primaryKeyConstraint.getColumns().size() > 1;
    }
}
