package com.github.yiuman.citrus.mda.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 数据库索引信息
 *
 * @author yiuman
 * @date 2021/4/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexMeta implements TableMetaRel {

    /**
     * 所属的表
     */
    private TableMeta table;

    /**
     * 索引名
     */
    private String indexName;

    /**
     * 是否唯一索引
     */
    private Boolean unique;

    /**
     * 索引列
     */
    private List<ColumnMeta> columns;

}
