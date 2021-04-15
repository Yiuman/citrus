package com.github.yiuman.citrus.dynamic;

import lombok.Data;

import java.util.List;

/**
 * @author yiuman
 * @date 2021/3/27
 */
@Data
public class TableEntity {

    private String tableName;

    private List<String> columns;

}
