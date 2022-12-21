package com.github.yiuman.citrus.system.rest;

import cn.hutool.core.util.StrUtil;
import com.github.yiuman.citrus.support.crud.query.annotations.Like;
import com.github.yiuman.citrus.support.crud.rest.BaseTreeController;
import com.github.yiuman.citrus.support.crud.service.TreeCrudService;
import com.github.yiuman.citrus.support.crud.view.impl.FormView;
import com.github.yiuman.citrus.support.crud.view.impl.PageTableView;
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

    @Override
    protected TreeCrudService<Organization, Long> getCrudService() {
        return organService;
    }

    @Override
    public Object createPageView() {
        PageTableView<Organization> view = new PageTableView<>();
        view.addWidget("组织名称", "organName");
        view.addColumn("组织机构名称", Organization::getOrganName);
        view.addColumn("组织机构代码", entity -> StrUtil.isBlank(entity.getOrganCode()) ? "-" : entity.getOrganCode());
        view.addColumn("描述", Organization::getRemark);
        view.defaultSetting();
        return view;
    }

    @SuppressWarnings("unchecked")
//    @Override
//    public <VIEW extends TreeView<Organization>> VIEW showTreeView(Organization data) {
//        PageTreeView<Organization> view = new PageTreeView<>();
//        view.setItemText("organName");
//        view.addWidget("组织名称", "organName");
//        view.addButton(Buttons.defaultButtonsWithMore());
//        return (VIEW) view;
//    }


    @Override
    public Object createFormView() {
        FormView formView = new FormView();
        formView.addEditField("组织机构名称", "organName").addRule("required");
        formView.addEditField("组织机构代码", "organCode").addRule("required");
        formView.addEditField("备注", "remark");
        return formView;
    }

    @Data
    static class OrganQuery {
        @Like(mapping = "organ_name")
        private String organName;
    }
}
