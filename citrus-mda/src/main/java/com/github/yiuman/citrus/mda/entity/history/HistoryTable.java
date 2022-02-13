package com.github.yiuman.citrus.mda.entity.history;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.yiuman.citrus.mda.entity.BaseTable;
import com.github.yiuman.citrus.mda.entity.TableRel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 历史数据表信息表
 *
 * @author yiuman
 * @date 2021/4/19
 */
@EqualsAndHashCode(callSuper = true)
@TableName("sys_hi_tables")
@Data
public class HistoryTable extends BaseTable implements TableRel {

    private String tableUuid;

    @TableField(exist = false)
    private List<HistoryColumn> columns;

    @TableField(exist = false)
    private List<HistoryIndexes> indexes;

}
