package com.github.yiuman.citrus.system.rest;

import com.github.yiuman.citrus.support.crud.rest.BaseCrudController;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.utils.Buttons;
import com.github.yiuman.citrus.system.dto.AuthorityDto;
import com.github.yiuman.citrus.system.dto.AuthorityQuery;
import com.github.yiuman.citrus.system.service.AuthorityService;
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
    protected Page<AuthorityDto> createPage() throws Exception {
        Page<AuthorityDto> page = super.createPage();
        page.addHeader("权限名称", "authorityName");
        page.addHeader("描述", "describe");

        page.addWidget("权限名称", "authorityName");

        page.addButton(Buttons.defaultButtons());
        page.addActions(Buttons.defaultActions());
        return page;
    }


}
