package com.github.yiuman.citrus.mda.entity.history;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.yiuman.citrus.mda.entity.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 列实体
 *
 * @author yiuman
 * @date 2021/4/19
 */
@EqualsAndHashCode(callSuper = true)
@TableName("sys_hi_columns")
@Data
public class HistoryColumn extends Column {
}
