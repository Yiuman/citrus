package com.github.yiuman.citrus.system.rest;

import com.github.yiuman.citrus.support.crud.query.annotations.Equals;
import com.github.yiuman.citrus.support.crud.query.annotations.Like;
import com.github.yiuman.citrus.support.crud.rest.BaseCrudController;
import com.github.yiuman.citrus.support.crud.view.impl.FormView;
import com.github.yiuman.citrus.support.crud.view.impl.PageTableView;
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

    @Override
    protected ResourceService getService() {
        return resourceService;
    }

    @Override
    public Object createPageView() {
        PageTableView<ResourceDto> view = new PageTableView<>();
        view.addColumn("资源名", "resourceName");
        view.addColumn("资源类型", "typeText", (entity) -> {
            String typeString = "";
            if (entity.getType() != null && entity.getType() == 0) {
                typeString = "菜单";
            }
            return typeString;
        });
        view.addColumn("资源路径", "path");
        view.addWidget("资源名", "resourceName");
        view.defaultSetting();
        return view;
    }

    @Override
    public Object createFormView() {
        FormView formView = new FormView();
        formView.addEditField("资源名", "resourceName");
        formView.addEditField("资源路径", "path");
        return formView;
    }

    @Data
    static class ResourceQuery {
        @Like(mapping = "resource_name")
        private String resourceName;
        @Equals(mapping = "parent_id")
        private Long parentId;
    }

}
