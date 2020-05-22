package com.github.yiuman.citrus.system.rest;

import com.github.yiuman.citrus.support.crud.rest.BaseCrudController;
import com.github.yiuman.citrus.support.model.DialogView;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.utils.Buttons;
import com.github.yiuman.citrus.system.dto.ResourceDto;
import com.github.yiuman.citrus.system.dto.ResourceQuery;
import com.github.yiuman.citrus.system.service.ResourceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 资源控制器
 *
 * @author yiuman
 * @date 2020/4/6
 */
@RestController
@RequestMapping("/rest/resources")
public class ResourceController extends BaseCrudController<ResourceDto, Long> {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
        setParamClass(ResourceQuery.class);
    }

    @Override
    protected ResourceService getService() {
        return resourceService;
    }

    @Override
    protected Page<ResourceDto> createPage() throws Exception {
        Page<ResourceDto> page = super.createPage();
        page.addHeader("资源名","resourceName");
        page.addHeader("资源类型","type");
        page.addHeader("资源路径","path");

        page.addWidget("资源名","resourceName");
        page.addButton(Buttons.defaultButtons());
        return page;
    }

    @Override
    protected DialogView createDialogView() throws Exception {
        DialogView dialogView = new DialogView();
        dialogView.addEditField("资源名","resourceName");
        dialogView.addEditField("资源路径","path");
        return dialogView;
    }

}
