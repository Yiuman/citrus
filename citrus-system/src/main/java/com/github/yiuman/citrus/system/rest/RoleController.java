package com.github.yiuman.citrus.system.rest;

import com.github.yiuman.citrus.support.crud.rest.BaseCrudController;
import com.github.yiuman.citrus.support.model.DialogView;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.utils.Buttons;
import com.github.yiuman.citrus.support.widget.Inputs;
import com.github.yiuman.citrus.system.dto.RoleDto;
import com.github.yiuman.citrus.system.dto.RoleQuery;
import com.github.yiuman.citrus.system.service.RoleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色控制器
 *
 * @author yiuman
 * @date 2020/4/6
 */
@RestController
@RequestMapping("/rest/roles")
public class RoleController extends BaseCrudController<RoleDto, Long> {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
        setParamClass(RoleQuery.class);
    }

    @Override
    protected RoleService getService() {
        return roleService;
    }


    @Override
    protected Page<RoleDto> createPage() throws Exception {
        Page<RoleDto> page = super.createPage();
        page.addHeader("ID", "roleId");
        page.addHeader("角色名", "roleName");
        page.addHeader("排序号", "orderId", true);

        page.addWidget("角色名", "roleName");
        //添加默认按钮
        page.addButton(Buttons.defaultButtons());
        //添加默认行内操作
        page.addActions(Buttons.defaultActions());
        return page;
    }

    @Override
    protected DialogView createDialogView()  {
        DialogView dialogView = new DialogView();
        dialogView.addEditField("角色名","roleName");
        return dialogView;
    }
}
