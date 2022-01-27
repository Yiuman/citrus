package com.github.yiuman.citrus.mda.ddl;

import com.github.yiuman.citrus.mda.exception.DdlException;
import com.github.yiuman.citrus.mda.mapper.DdlMapper;
import com.github.yiuman.citrus.mda.meta.ColumnMeta;
import com.github.yiuman.citrus.mda.meta.IndexMeta;
import com.github.yiuman.citrus.mda.meta.TableMeta;
import com.github.yiuman.citrus.support.datasource.DynamicDataSourceHolder;
import com.github.yiuman.citrus.support.datasource.DynamicSqlSessionTemplate;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * DDL处理器实现
 *
 * @author yiuman
 * @date 2021/4/26
 */
public class DdlProcessorImpl extends BaseDdlProcessor {

    private final DynamicSqlSessionTemplate dynamicSqlSessionTemplate;

    public DdlProcessorImpl(DynamicSqlSessionTemplate dynamicSqlSessionTemplate) {
        this.dynamicSqlSessionTemplate = dynamicSqlSessionTemplate;
    }

    protected DdlMapper getDdlMapper(String namespace) {
        return getSqlSessionFactory(namespace).openSession().getMapper(DdlMapper.class);
    }

    @Override
    public SqlSessionFactory getSqlSessionFactory(String namespace) {
        DynamicDataSourceHolder.push(namespace);
        return dynamicSqlSessionTemplate.getSqlSessionFactory();
    }

    @Override
    public boolean exist(TableMeta tableMeta) {
        return getDdlMapper(tableMeta.getNamespace()).exist(tableMeta.getTableName(), tableMeta.getNamespace());
    }

    @Override
    public void createTable(TableMeta tableMeta) {
        doAction(tableMeta, Action.CREATE);
    }


    @Override
    public void dropTable(TableMeta tableMeta) {
        doAction(tableMeta, Action.DELETE);
    }

    @Override
    public void createIndex(IndexMeta indexMeta) throws DdlException {
        doAction(indexMeta, Action.CREATE);
    }

    @Override
    public void dropIndex(IndexMeta indexMeta) throws DdlException {
        doAction(indexMeta, Action.DELETE);
    }

    @Override
    public void createColumn(ColumnMeta columnMeta) throws DdlException {
        doAction(columnMeta, Action.CREATE);
    }

    @Override
    public void updateColumn(ColumnMeta columnMeta) throws DdlException {
        doAction(columnMeta, Action.UPDATE);
    }

    @Override
    public void deleteColumn(ColumnMeta columnMeta) throws DdlException {
        doAction(columnMeta, Action.DELETE);
    }


}
