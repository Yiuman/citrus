package com.github.yiuman.citrus.mda.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 列与索引的关联表
 *
 * @author yiuman
 * @date 2021/4/19
 */
@TableName("sys_columns_indexes")
@EqualsAndHashCode(callSuper = true)
@Data
public class ColumnsIndexes extends BaseColumnRel {

    private String indexUuid;

}
