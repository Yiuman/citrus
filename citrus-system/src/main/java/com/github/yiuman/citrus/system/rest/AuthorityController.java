package com.github.yiuman.citrus.system.rest;

import com.github.yiuman.citrus.support.crud.query.annotations.Like;
import com.github.yiuman.citrus.support.crud.rest.BaseCrudController;
import com.github.yiuman.citrus.support.crud.view.impl.PageTableView;
import com.github.yiuman.citrus.system.dto.AuthorityDto;
import com.github.yiuman.citrus.system.service.AuthorityService;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限控制器
 *
 * @author yiuman
 * @date 2020/4/4
 */
@RestController
@RequestMapping("/rest/auth")
public class AuthorityController extends BaseCrudController<AuthorityDto, Long> {

    private final AuthorityService authorityService;

    public AuthorityController(AuthorityService authorityService) {
        setParamClass(AuthorityQuery.class);
        this.authorityService = authorityService;
    }

    @Override
    protected AuthorityService getService() {
        return authorityService;
    }

    @Override
    public Object createPageView() {
        PageTableView<AuthorityDto> view = new PageTableView<>();
        view.addWidget("权限名称", "authorityName");
        view.addColumn("权限名称", "authorityName");
        view.addColumn("描述", "remark");
        view.defaultSetting();
        return view;
    }

    @Data
    static class AuthorityQuery {
        @Like
        private String authorityName;
    }
}
