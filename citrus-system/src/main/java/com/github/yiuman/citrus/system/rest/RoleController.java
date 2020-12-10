package com.github.yiuman.citrus.system.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.support.crud.query.QueryParam;
import com.github.yiuman.citrus.support.crud.rest.BaseCrudController;
import com.github.yiuman.citrus.support.model.DialogView;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.utils.Buttons;
import com.github.yiuman.citrus.support.utils.CrudUtils;
import com.github.yiuman.citrus.support.widget.Selects;
import com.github.yiuman.citrus.system.dto.AuthorityDto;
import com.github.yiuman.citrus.system.dto.RoleDto;
import com.github.yiuman.citrus.system.entity.Authority;
import com.github.yiuman.citrus.system.service.AuthorityService;
import com.github.yiuman.citrus.system.service.RoleService;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private final AuthorityService authorityService;

    public RoleController(RoleService roleService, AuthorityService authorityService) {
        this.roleService = roleService;
        this.authorityService = authorityService;
        setParamClass(RoleQuery.class);
    }

    @Data
    static class RoleQuery {

        @QueryParam(type = "like")
        private String roleName;

        private List<Long> authIds;
    }


    @Override
    protected RoleService getService() {
        return roleService;
    }

    @Override
    protected QueryWrapper<RoleDto> getQueryWrapper(Object params) {
        QueryWrapper<RoleDto> queryWrapper = super.getQueryWrapper(params);
        RoleQuery roleQuery = (RoleQuery) params;
        List<Long> authIds = roleQuery.getAuthIds();
        //拼接权限ID筛选条件
        if (roleQuery != null && !org.springframework.util.CollectionUtils.isEmpty(authIds)) {
            queryWrapper = Optional.ofNullable(queryWrapper).orElse(Wrappers.query());

            String inSql = String.format(
                    "select role_id from sys_role_auth where authority_id in (%s)"
                    , authIds.parallelStream().map(String::valueOf).collect(Collectors.joining(","))
            );
            queryWrapper.inSql(getService().getKeyColumn(), inSql);
        }

        return queryWrapper;
    }

    @Override
    protected Page<RoleDto> createPage() throws Exception {
        Page<RoleDto> page = super.createPage();
        page.addHeader("ID", "roleId");
        page.addHeader("角色名", "roleName");
        page.addHeader("拥有权限", "authNames", (entity) -> {
            List<Authority> authorities = roleService.getAuthoritiesByRoleId(entity.getRoleId());
            if (CollectionUtils.isEmpty(authorities)) {
                return "-";
            }
            entity.setAuthIds(authorities.parallelStream().map(Authority::getAuthorityId).collect(Collectors.toList()));
            return authorities.parallelStream().map(Authority::getAuthorityName).collect(Collectors.joining(","));
        });

        page.addWidget("角色名", "roleName");
        //添加默认按钮
        page.addButton(Buttons.defaultButtonsWithMore());
        //添加默认行内操作
        page.addActions(Buttons.defaultActions());
        return page;
    }

    @Override
    protected DialogView createDialogView() throws InvocationTargetException, IllegalAccessException {
        DialogView dialogView = new DialogView();
        dialogView.addEditField("角色名", "roleName").addRule("required");
        dialogView.addEditField("选择权限", "authIds", CrudUtils.getWidget(this, "getAuthorities"));
        return dialogView;
    }

    @Selects(bind = "authIds", key = "authorityId", label = "authorityName", text = "选择权限", multiple = true)
    public List<AuthorityDto> getAuthorities() {
        return authorityService.list();
    }

}
