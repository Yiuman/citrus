package com.github.yiuman.citrus.system.rest;

import com.github.yiuman.citrus.support.crud.query.QueryParam;
import com.github.yiuman.citrus.support.crud.rest.BaseCrudController;
import com.github.yiuman.citrus.support.crud.view.impl.DialogView;
import com.github.yiuman.citrus.support.crud.view.impl.PageTableView;
import com.github.yiuman.citrus.support.utils.Buttons;
import com.github.yiuman.citrus.system.dto.ResourceDto;
import com.github.yiuman.citrus.system.service.ResourceService;
import lombok.Data;
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

    @Data
    static class ResourceQuery {

        @QueryParam(type = "like")
        private String resourceName;

        @QueryParam
        private Long parentId;
    }


    @Override
    protected ResourceService getService() {
        return resourceService;
    }

    @Override
    protected Object createView() {
        PageTableView<ResourceDto> view = new PageTableView<>();
        view.addHeader("资源名", "resourceName");
        view.addHeader("资源类型", "typeText", (entity) -> {
            String typeString = "";
            if (entity.getType() != null && entity.getType() == 0) {
                typeString = "菜单";
            }
            return typeString;
        });
        view.addHeader("资源路径", "path");
        view.addWidget("资源名", "resourceName");
        view.addButton(Buttons.defaultButtonsWithMore());
        view.addAction(Buttons.defaultActions());
        return view;
    }


    @Override
    protected Object createEditableView() {
        DialogView dialogView = new DialogView();
        dialogView.addEditField("资源名", "resourceName");
        dialogView.addEditField("资源路径", "path");
        return dialogView;
    }

}
