package com.github.yiuman.citrus.mda.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 表实体
 *
 * @author yiuman
 * @date 2021/4/19
 */
@EqualsAndHashCode(callSuper = false)
@TableName("sys_tables")
@Data
public class Table extends BaseTable {

    @TableField(exist = false)
    private List<Column> columns;

    @TableField(exist = false)
    private List<Indexes> indexes;

}
