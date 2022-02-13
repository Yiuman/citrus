package com.github.yiuman.citrus.system.rest;

import com.github.yiuman.citrus.support.crud.rest.BaseTreeController;
import com.github.yiuman.citrus.support.crud.service.TreeCrudService;
import com.github.yiuman.citrus.support.crud.view.TreeView;
import com.github.yiuman.citrus.support.crud.view.impl.PageTreeView;
import com.github.yiuman.citrus.support.http.ResponseEntity;
import com.github.yiuman.citrus.support.utils.Buttons;
import com.github.yiuman.citrus.system.entity.Resource;
import com.github.yiuman.citrus.system.service.MenuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        setParamClass(ResourceController.ResourceQuery.class);
        this.menuService = menuService;
    }

    @Override
    protected TreeCrudService<Resource, Long> getCrudService() {
        return menuService;
    }

    @Override
    public TreeView<Resource> showTreeView(Resource tree) {
        PageTreeView<Resource> view = new PageTreeView<>();
        view.setTree(tree);
        view.setItemText("resourceName");
        view.addWidget("菜单名称", "resourceName");
//        view.addAction("操作资源", "this.$router.push(`/rest/resources?parentId=${this.currentItem.id}`)");
        view.addButton(Buttons.defaultButtonsWithMore());
        return view;
    }

//    @Override
//    protected Object createEditableView() {
//        DialogView view = new DialogView();
//        view.addEditField("菜单名称", "resourceName").addRule("required");
//        view.addEditField("路径", "path");
//        view.addEditField("组件", "component");
//        view.addEditField("图标代码", "icon");
//        return view;
//    }

    @GetMapping("/operation/{key}")
    public ResponseEntity<List<Resource>> getOperationByKey(@PathVariable Long key) {
        return ResponseEntity.ok(menuService.getOperationByKey(key));
    }
}
