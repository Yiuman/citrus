package com.github.yiuman.citrus.support.crud.view;

/**
 * 有复选框的表格视图
 *
 * @author yiuman
 * @date 2021/1/19
 */
public interface CheckboxTableView extends TableView {

    /**
     * 是否可勾选
     *
     * @return true/false
     */
    boolean getCheckable();

}