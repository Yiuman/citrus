package com.github.yiuman.citrus.system.rest;

import com.github.yiuman.citrus.support.crud.rest.BaseTreeController;
import com.github.yiuman.citrus.support.crud.service.TreeCrudService;
import com.github.yiuman.citrus.support.model.DialogView;
import com.github.yiuman.citrus.support.model.TreeDisplay;
import com.github.yiuman.citrus.support.utils.Buttons;
import com.github.yiuman.citrus.system.dto.OrganQuery;
import com.github.yiuman.citrus.system.entity.Organization;
import com.github.yiuman.citrus.system.service.OrganService;
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

    @Override
    protected TreeCrudService<Organization, Long> getCrudService() {
        return organService;
    }

    @Override
    protected TreeDisplay<Organization> createTree() throws Exception {
        TreeDisplay<Organization> tree = super.createTree();
        tree.setItemText("organName");
        tree.addWidget("组织名称", "organName");
        tree.addButton(Buttons.defaultButtons());
        return tree;
    }

    @Override
    protected DialogView createDialogView() throws Exception {
        DialogView dialogView = new DialogView();
        dialogView.addEditField("组织机构名称","organName");
        dialogView.addEditField("组织机构代码","organCode");
        dialogView.addEditField("备注","remark");
        return dialogView;
    }
}
