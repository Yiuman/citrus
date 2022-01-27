package com.github.yiuman.citrus.mda.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 表格列表列信息
 *
 * @author yiuman
 * @date 2021/4/20
 */
@Data
@TableName("sys_display_column")
public class DisplayColumn implements TableRel {

    @TableId(type = IdType.ASSIGN_ID)
    private String uuid;

    private String tableUuid;

    /**
     * 对应数据库表的列
     */
    private String columnUuid;

    /**
     * 别名
     */
    private String columnAlias;

    /**
     * 对应数据库列的列名
     */
    private String columnName;

    /**
     * 表达式，当computed为true时，这里非空，用于动态构造出显示的列信息
     */
    private String expression;

}
