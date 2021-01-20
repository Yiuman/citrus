package com.github.yiuman.citrus.support.crud.view;

import com.github.yiuman.citrus.support.model.Header;

import java.util.List;

/**
 * 表格视图
 *
 * @author yiuman
 * @date 2021/1/19
 */
public interface TableView extends View {

    List<Header> getHeaders();
}