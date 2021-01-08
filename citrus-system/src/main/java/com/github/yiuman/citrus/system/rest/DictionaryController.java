package com.github.yiuman.citrus.system.rest;

import com.github.yiuman.citrus.support.crud.rest.BaseCrudController;
import com.github.yiuman.citrus.support.model.DialogView;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.utils.Buttons;
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
    protected Page<Dictionary> createPage() throws Exception {
        Page<Dictionary> page = super.createPage();
        page.addHeader("名称", "dictName");
        page.addHeader("编码", "dictCode");
        page.addButton(Buttons.defaultButtonsWithMore());
        page.addActions(Buttons.defaultActions());
        return page;
    }

    @Override
    protected DialogView createDialogView() {
        DialogView view = new DialogView();
        view.addEditField("名称", "dictName").addRule("required");
        view.addEditField("编码", "dictCode").addRule("required");
        return view;
    }
}
