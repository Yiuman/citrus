package com.github.yiuman.citrus.mda.entity.history;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.yiuman.citrus.mda.entity.BaseColumnRel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 列与索引的关联表
 *
 * @author yiuman
 * @date 2021/4/19
 */
@TableName("sys_hi_columns_indexes")
@EqualsAndHashCode(callSuper = true)
@Data
public class HistoryColumnsIndexes extends BaseColumnRel {

    private String indexUuid;

}
