package com.github.yiuman.citrus.support.crud.view;

import com.github.yiuman.citrus.support.model.Column;

import java.util.List;

/**
 * 表格视图
 *
 * @author yiuman
 * @date 2021/1/19
 */
public interface TableView extends View {

    /**
     * 获取表头集合
     *
     * @return 表格信息集合
     */
    List<Column> getHeaders();
}