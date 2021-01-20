package com.github.yiuman.citrus.system.rest;

import com.github.yiuman.citrus.support.crud.query.QueryParam;
import com.github.yiuman.citrus.support.crud.rest.BaseTreeController;
import com.github.yiuman.citrus.support.crud.service.TreeCrudService;
import com.github.yiuman.citrus.support.crud.view.TreeView;
import com.github.yiuman.citrus.support.crud.view.impl.DialogView;
import com.github.yiuman.citrus.support.crud.view.impl.PageTreeView;
import com.github.yiuman.citrus.support.utils.Buttons;
import com.github.yiuman.citrus.system.entity.Organization;
import com.github.yiuman.citrus.system.service.OrganService;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 组织机构控制器
 *
 * @author yiuman
 * @date 2020/4/6
 */
@RestController
@RequestMapping("/rest/organs")
public class OrganController extends BaseTreeController<Organization, Long> {

    private final OrganService organService;

    public OrganController(OrganService organService) {
        setParamClass(OrganQuery.class);
        this.organService = organService;
    }

    @Data
    static class OrganQuery {

        @QueryParam(type = "like")
        private String organName;

    }

    @Override
    protected TreeCrudService<Organization, Long> getCrudService() {
        return organService;
    }

    @Override
    protected TreeView<Organization> createTreeView() throws Exception {
        PageTreeView<Organization> view = new PageTreeView<>();
        view.setItemText("organName");
        view.addWidget("组织名称", "organName");
        view.addButton(Buttons.defaultButtonsWithMore());
        return view;
    }


    @Override
    protected DialogView createEditableView() throws Exception {
        DialogView dialogView = new DialogView();
        dialogView.addEditField("组织机构名称", "organName").addRule("required");
        dialogView.addEditField("组织机构代码", "organCode").addRule("required");
        dialogView.addEditField("备注", "remark");
        return dialogView;
    }
}
