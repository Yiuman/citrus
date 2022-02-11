package com.github.yiuman.citrus.support.widget;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 列组
 *
 * @author yiuman
 * @date 2022/2/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ColumnGroup extends BaseColumn {

    private List<Column> children;
}
