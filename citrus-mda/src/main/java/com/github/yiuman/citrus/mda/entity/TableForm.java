package com.github.yiuman.citrus.mda.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 表的动态表单信息
 *
 * @author yiuman
 * @date 2021/4/20
 */
@Data
@TableName("sys_table_form")
public class TableForm implements TableRel {

    @TableId(type = IdType.ASSIGN_UUID)
    private String uuid;

    private String tableUuid;

}
