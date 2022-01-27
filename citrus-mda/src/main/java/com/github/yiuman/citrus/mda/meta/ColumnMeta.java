package com.github.yiuman.citrus.mda.meta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.JDBCType;
import java.util.Objects;

/**
 * 数据库列信息
 *
 * @author yiuman
 * @date 2021/4/19
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnMeta implements TableMetaRel {

    /**
     * 列名（字段名）
     */
    private String columnName;

    private TableMeta table;

    /**
     * 字段类型
     *
     * @see JDBCType
     */
    private JDBCType jdbcType;


    /**
     * 精度，如果为0表示自动决定长度
     * 如：varchar(200)  这里指200的精度
     */
    private Integer precisions;

    /**
     * 小数点
     * 如： decimal(18, 2) 这里指的是2小数点位
     */
    private Integer scales;


    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 是否非空
     */
    private Boolean notNull;

    /**
     * 是否主键
     */
    private Boolean primaryKey;

    /**
     * 是否唯一
     */
    private Boolean uniques;

    /**
     * 字段描述
     */
    private String comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ColumnMeta)) {
            return false;
        }

        ColumnMeta that = (ColumnMeta) o;

        if (!Objects.equals(columnName, that.columnName)) {
            return false;
        }
        return jdbcType == that.jdbcType;
    }

    @Override
    public int hashCode() {
        int result = columnName != null ? columnName.hashCode() : 0;
        result = 31 * result + (jdbcType != null ? jdbcType.hashCode() : 0);
        return result;
    }
}
