package com.github.yiuman.citrus.mda.mapper;

import com.github.yiuman.citrus.mda.meta.ColumnMeta;
import com.github.yiuman.citrus.mda.meta.IndexMeta;
import com.github.yiuman.citrus.mda.meta.TableMeta;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 数据库DDL相关操作Mapper
 *
 * @author yiuman
 * @date 2021/4/20
 */
@Mapper
@Repository
public interface DdlMapper {

    /**
     * 是否存在
     *
     * @param tableName 表名
     * @param namespace 命名空间 如mysql则是table_schema，又如oracle这里的namespace则是用户实例名
     * @return true/false 存在返回true
     */
    boolean exist(@Param("tableName") String tableName, @Param("namespace") String namespace);

    /**
     * 重名名表
     *
     * @param tableName 原表名
     * @param namespace 命名空间
     * @param newName   新表名
     */
    void rename(@Param("tableName") String tableName, @Param("namespace") String namespace, @Param("newName") String newName);

    /**
     * 获取表的行数
     *
     * @param tableName 表名
     * @param namespace 命名空间
     * @return 数据行数
     */
    Integer getTableRows(@Param("tableName") String tableName, @Param("namespace") String namespace);

    /**
     * 获取某表的建表语句
     *
     * @param tableName 表名
     * @param namespace 命名空间
     * @return 建议sql脚本语句
     */
    String getGenerateSql(@Param("tableName") String tableName, @Param("namespace") String namespace);

    /**
     * 创表
     *
     * @param tableMeta 表定义信息
     */
    void createTable(TableMeta tableMeta);

    /**
     * 删表
     *
     * @param tableMeta 表信息
     */
    void dropTable(TableMeta tableMeta);

    /**
     * 创建索引
     *
     * @param indexMeta 索引信息
     */
    void createIndex(IndexMeta indexMeta);

    /**
     * 删除索引
     *
     * @param indexMeta 索引信息
     */
    void dropIndex(IndexMeta indexMeta);


    /**
     * 创建列
     *
     * @param columnMeta 列的元信息
     */
    void createColumn(ColumnMeta columnMeta);

    /**
     * 更新列
     *
     * @param columnMeta 列的元信息
     */
    void updateColumn(ColumnMeta columnMeta);

    /**
     * 删除列
     *
     * @param columnMeta 列的元信息
     */
    void deleteColumn(ColumnMeta columnMeta);
}
