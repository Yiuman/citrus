package com.github.yiuman.citrus.support.crud.view;

import com.github.yiuman.citrus.support.crud.rest.BaseQueryRestful;
import com.github.yiuman.citrus.support.crud.rest.BaseTreeController;
import com.github.yiuman.citrus.support.crud.view.impl.PageTableView;
import com.github.yiuman.citrus.support.crud.view.impl.PageTreeView;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.model.Tree;
import com.github.yiuman.citrus.support.utils.CrudUtils;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;

/**
 * @author yiuman
 * @date 2022/1/23
 */
public final class ViewHelper {
    private ViewHelper() {
    }

    public static <T, K extends Serializable, CRUD extends BaseQueryRestful<T, K>> PageTableView<T> createPageView(CRUD crud, Page<T> data) {
        PageTableView<T> pageTableView = new PageTableView<>();
        pageTableView.setData(data);
        //构造页面小部件
        CrudUtils.getCrudWidgets(crud).forEach(widget -> pageTableView.addWidget(widget, true));
        //构造默认表头
        ReflectionUtils.doWithFields(crud.getModelClass(), field -> pageTableView.addColumn(field.getName(), field.getName()));
        return pageTableView;
    }

    public static <K extends Serializable, T extends Tree<K>, CRUD extends BaseTreeController<T, K>> PageTreeView<T> createTreeView(CRUD crud, T data) {
        PageTreeView<T> pageTreeView = new PageTreeView<>();
        pageTreeView.setTree(data);
        //构造页面小部件
        CrudUtils.getCrudWidgets(crud).forEach(widget -> pageTreeView.addWidget(widget, true));
        //构造默认表头
        return pageTreeView;
    }
}
