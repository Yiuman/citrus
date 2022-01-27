package com.github.yiuman.citrus.mda.ddl;

import com.github.yiuman.citrus.mda.exception.DdlException;
import com.github.yiuman.citrus.mda.meta.ColumnMeta;
import com.github.yiuman.citrus.mda.meta.IndexMeta;
import com.github.yiuman.citrus.mda.meta.TableMeta;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * ddl处理器
 *
 * @author yiuman
 * @date 2021/4/26
 */
public interface DdlProcessor {

    /**
     * 根据数据源获取SqlSessionFactory
     *
     * @param namespace 名称空间，这里主要实现的拿到对应的数据源
     * @return SqlSessionFactory
     */
    SqlSessionFactory getSqlSessionFactory(String namespace);

    /**
     * 判断此元数据表是否存在
     *
     * @param tableMeta 表元数据信息
     * @return true/false
     * @throws DdlException ddl操作相关异常
     */
    boolean exist(TableMeta tableMeta) throws DdlException;

    /**
     * 创建表
     *
     * @param tableMeta 表元数据模型
     * @throws DdlException ddl操作相关异常
     */
    void createTable(TableMeta tableMeta) throws DdlException;

    /**
     * 删除表
     *
     * @param tableMeta 表的元数据模型
     * @throws DdlException ddl操作相关异常
     */
    void dropTable(TableMeta tableMeta) throws DdlException;

    /**
     * 创建索引
     *
     * @param indexMeta 索引信息
     * @throws DdlException ddl操作相关异常
     */
    void createIndex(IndexMeta indexMeta) throws DdlException;

    /**
     * 删除索引
     *
     * @param indexMeta 索引信息
     * @throws DdlException ddl操作相关异常
     */
    void dropIndex(IndexMeta indexMeta) throws DdlException;


    /**
     * 创建列
     *
     * @param columnMeta 列的元信息
     * @throws DdlException ddl操作相关异常
     */
    void createColumn(ColumnMeta columnMeta) throws DdlException;

    /**
     * 更新列
     *
     * @param columnMeta 列的元信息
     * @throws DdlException ddl操作相关异常
     */
    void updateColumn(ColumnMeta columnMeta) throws DdlException;

    /**
     * 删除列
     *
     * @param columnMeta 列的元信息
     * @throws DdlException ddl操作相关异常
     */
    void deleteColumn(ColumnMeta columnMeta) throws DdlException;
}