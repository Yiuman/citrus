package com.github.yiuman.citrus.system.rest;

import com.github.yiuman.citrus.support.crud.rest.BaseCrudController;
import com.github.yiuman.citrus.support.crud.view.impl.DialogView;
import com.github.yiuman.citrus.support.crud.view.impl.PageTableView;
import com.github.yiuman.citrus.support.utils.Buttons;
import com.github.yiuman.citrus.system.entity.Dictionary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    protected Object createView(List<Dictionary> records) {
        PageTableView<Dictionary> view = new PageTableView<>();
        view.addHeader("名称", "dictName");
        view.addHeader("编码", "dictCode");
        view.addButton(Buttons.defaultButtonsWithMore());
        view.addAction(Buttons.defaultActions());
        return view;
    }

    @Override
    protected Object createEditableView() {
        DialogView view = new DialogView();
        view.addEditField("名称", "dictName").addRule("required");
        view.addEditField("编码", "dictCode").addRule("required");
        return view;
    }
}
