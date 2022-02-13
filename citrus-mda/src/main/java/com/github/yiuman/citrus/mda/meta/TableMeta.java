package com.github.yiuman.citrus.mda.meta;

import cn.hutool.core.collection.CollectionUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 数据库表信息
 *
 * @author yiuman
 * @date 2021/4/19
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableMeta implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 命名空间
     * 如：mysql的`database`与`schema`其实是的等价的，这里的`namespace`就是`schema`;
     * 又如有`catalog`的，那么这里的是`namespace`就是 `catalog.schema`
     */
    private String namespace;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表的列信息
     */
    private List<ColumnMeta> columns;

    /**
     * 表的约束条件
     */
    private List<Constraint> constraints;

    /**
     * 表的索引信息
     */
    private List<IndexMeta> indexes;

    /**
     * 表的描述信息
     */
    private String comments;

    public PrimaryKeyConstraint getPrimaryKey() {
        if (CollectionUtil.isEmpty(constraints)) {
            return null;
        }

        return (PrimaryKeyConstraint) constraints.stream().filter(constraint -> constraint.getTypeName().equals(Constraint.PRIMARY))
                .findFirst().orElse(null);

    }

}
