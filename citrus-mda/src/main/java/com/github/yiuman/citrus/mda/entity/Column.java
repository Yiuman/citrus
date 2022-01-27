package com.github.yiuman.citrus.mda.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.JDBCType;

/**
 * 列实体
 *
 * @author yiuman
 * @date 2021/4/19
 */
@TableName("sys_columns")
@Data
public class Column implements TableRel {

    @TableId(type = IdType.ASSIGN_UUID)
    private String uuid;

    private String tableUuid;

    private String columnName;

    private JDBCType jdbcType;

    private Integer precisions;

    private Integer scales;

    private String defaultValue;

    private String comments;

    private Boolean notNull;

    private Boolean primaryKey;

    private Boolean uniques;

}
