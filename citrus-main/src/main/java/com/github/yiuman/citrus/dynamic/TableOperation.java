package com.github.yiuman.citrus.dynamic;

/**
 * @author yiuman
 * @date 2021/3/27
 */
public class TableOperation {

    public TableOperation() {
    }


    public String provideSql(TableEntity entity) {
        StringBuilder createSqlBuilder = new StringBuilder("create table ${tableName} (");
        entity.getColums().forEach(item -> {
            createSqlBuilder.append(item).append(" varchar(50)");
            if (entity.getColums().indexOf(item) != entity.getColums().size() - 1) {
                createSqlBuilder.append(" , ");
            }
        });
        createSqlBuilder.append(")");
        return createSqlBuilder.toString();
    }
}
