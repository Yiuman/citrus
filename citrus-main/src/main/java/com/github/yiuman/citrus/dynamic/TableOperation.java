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
        entity.getColumns().forEach(item -> {
            createSqlBuilder.append(item).append(" varchar(50)");
            if (entity.getColumns().indexOf(item) != entity.getColumns().size() - 1) {
                createSqlBuilder.append(" , ");
            }
        });
        createSqlBuilder.append(")");
        return createSqlBuilder.toString();
    }
}
