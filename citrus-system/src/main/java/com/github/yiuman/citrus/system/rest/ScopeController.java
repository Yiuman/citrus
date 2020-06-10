package com.github.yiuman.citrus.system.rest;

import com.github.yiuman.citrus.support.crud.rest.BaseCrudController;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.model.DialogView;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.utils.Buttons;
import com.github.yiuman.citrus.system.dto.ScopeDto;
import com.github.yiuman.citrus.system.entity.Organization;
import com.github.yiuman.citrus.system.service.OrganService;
import com.github.yiuman.citrus.system.service.ScopeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据范围CRUD
 *
 * @author yiuman
 * @date 2020/6/1
 */
@RestController
@RequestMapping("/rest/scopes")
public class ScopeController extends BaseCrudController<ScopeDto, Long> {

    private final ScopeService scopeService;

    private final OrganService organService;

    public ScopeController(ScopeService scopeService, OrganService organService) {
        this.scopeService = scopeService;
        this.organService = organService;
    }

    @Override
    protected CrudService<ScopeDto, Long> getService() {
        return scopeService;
    }

    @Override
    protected Page<ScopeDto> createPage() throws Exception {
        Page<ScopeDto> page = super.createPage();
        page.addHeader("数据范围名称", "scopeName");
        page.addHeader("所属组织", "organName", (entity) -> {
            if (-1 == entity.getOrganId()) {
                return "系统通用数据范围";
            }
            Organization organization = organService.get(entity.getOrganId());
            return organization == null ? "" : organization.getOrganName();
        });

        page.addButton(Buttons.defaultButtons());
        page.addActions(Buttons.defaultActions());
        return page;
    }

    @Override
    protected DialogView createDialogView() throws Exception {
        DialogView dialogView = new DialogView();
        dialogView.addEditField("数据范围名称","scopeName");
        dialogView.addEditField("所属组织","organId", organService.getOrganTree("选择机构","organId",false));
        return dialogView;
    }
}
