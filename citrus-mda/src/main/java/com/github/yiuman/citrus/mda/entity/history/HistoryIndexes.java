package com.github.yiuman.citrus.mda.entity.history;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.yiuman.citrus.mda.entity.BaseIndexes;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 索引实体
 *
 * @author yiuman
 * @date 2021/4/19
 */
@EqualsAndHashCode(callSuper = true)
@TableName("sys_hi_indexes")
@Data
public class HistoryIndexes extends BaseIndexes {

    @TableField(exist = false)
    private List<HistoryColumn> columns;

}
