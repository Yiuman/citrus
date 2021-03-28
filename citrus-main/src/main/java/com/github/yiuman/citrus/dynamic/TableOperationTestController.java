package com.github.yiuman.citrus.dynamic;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @author yiuman
 * @date 2021/3/27
 */
@RestController
public class TableOperationTestController {

    private final TableOperateMapper tableOperateMapper;

    public TableOperationTestController(TableOperateMapper tableOperateMapper) {
        this.tableOperateMapper = tableOperateMapper;
    }

    @GetMapping("/test/testCreateTable")
    public void testCreateTable() {
        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName("sys_test_create");
        tableEntity.setColums(Arrays.asList("id", "name"));
        tableOperateMapper.createTable(tableEntity);
    }
}
