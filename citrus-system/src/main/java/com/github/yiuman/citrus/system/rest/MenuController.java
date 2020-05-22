package com.github.yiuman.citrus.system.rest;

import com.github.yiuman.citrus.support.crud.rest.BaseTreeController;
import com.github.yiuman.citrus.support.crud.service.TreeCrudService;
import com.github.yiuman.citrus.support.model.DialogView;
import com.github.yiuman.citrus.support.model.TreeDisplay;
import com.github.yiuman.citrus.support.utils.Buttons;
import com.github.yiuman.citrus.system.entity.Organization;
import com.github.yiuman.citrus.system.entity.Resource;
import com.github.yiuman.citrus.system.service.MenuService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜单管理
 *
 * @author yiuman
 * @date 2020/5/22
 */
@RestController
@RequestMapping("/rest/menus")
public class MenuController extends BaseTreeController<Resource, Long> {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @Override
    protected TreeCrudService<Resource, Long> getCrudService() {
        return menuService;
    }

    @Override
    protected TreeDisplay<Resource> createTree() throws Exception {
        TreeDisplay<Resource> tree = super.createTree();
        tree.setItemText("resourceName");
        tree.addWidget("菜单名称", "resourceName");
        tree.addButton(Buttons.defaultButtons());
        return tree;
    }

    @Override
    protected DialogView createDialogView() throws Exception {
        DialogView view = new DialogView();
        view.addEditField("菜单名称","resourceName");
        view.addEditField("路径","path");
        view.addEditField("路径","path");
        return super.createDialogView();
    }
}
