package com.github.yiuman.citrus.system.rest;

import com.github.yiuman.citrus.support.crud.rest.BaseCrudController;
import com.github.yiuman.citrus.support.crud.view.impl.FormView;
import com.github.yiuman.citrus.support.crud.view.impl.PageTableView;
import com.github.yiuman.citrus.system.entity.Dictionary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 字典管理
 *
 * @author yiuman
 * @date 2020/7/31
 */
@RestController
@RequestMapping("/rest/dicts")
public class DictionaryController extends BaseCrudController<Dictionary, Long> {

    @Override
    public Object createPageView() {
        PageTableView<Dictionary> view = new PageTableView<>();
        view.addColumn("名称", "dictName");
        view.addColumn("编码", "dictCode");
        view.defaultSetting();
        return view;
    }

    @Override
    public Object createFormView() {
        FormView view = new FormView();
        view.addEditField("名称", "dictName").addRule("required");
        view.addEditField("编码", "dictCode").addRule("required");
        return view;
    }
}
