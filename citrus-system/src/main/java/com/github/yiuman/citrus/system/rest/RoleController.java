package com.github.yiuman.citrus.system.rest;

import cn.hutool.core.collection.CollUtil;
import com.github.yiuman.citrus.support.crud.query.Query;
import com.github.yiuman.citrus.support.crud.query.annotations.Like;
import com.github.yiuman.citrus.support.crud.query.builder.QueryBuilders;
import com.github.yiuman.citrus.support.crud.rest.BaseCrudController;
import com.github.yiuman.citrus.support.crud.view.impl.DialogView;
import com.github.yiuman.citrus.support.crud.view.impl.PageTableView;
import com.github.yiuman.citrus.support.utils.Buttons;
import com.github.yiuman.citrus.support.utils.CrudUtils;
import com.github.yiuman.citrus.support.widget.Selects;
import com.github.yiuman.citrus.system.dto.AuthorityDto;
import com.github.yiuman.citrus.system.dto.RoleDto;
import com.github.yiuman.citrus.system.entity.RoleAuthority;
import com.github.yiuman.citrus.system.service.AuthorityService;
import com.github.yiuman.citrus.system.service.RoleService;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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
        @Like(mapping = "role_name")
        private String roleName;
        private List<Long> authIds;
    }


    @Override
    protected RoleService getService() {
        return roleService;
    }

    @Override
    protected Query getQueryCondition(Object params) {
        Query query = super.getQueryCondition(params);
        RoleQuery roleQuery = (RoleQuery) params;
        List<Long> authIds = roleQuery.getAuthIds();
        //拼接权限ID筛选条件
        if (roleQuery != null && CollUtil.isNotEmpty(authIds)) {
            query = Optional.ofNullable(query).orElse(Query.create());

            String inSql = String.format(
                    "select role_id from sys_role_auth where authority_id in (%s)",
                    authIds.parallelStream().map(String::valueOf).collect(Collectors.joining(","))
            );
            QueryBuilders.wrapper(query).inSql(getService().getKeyColumn(), inSql);
        }


        return query;
    }

    @Override
    protected Object createView(List<RoleDto> roleDtos) {
        List<RoleAuthority> roleAuthorities = roleService.getRoleAuthorityByRoleIds(roleDtos.stream().map(RoleDto::getRoleId).collect(Collectors.toSet()));
        PageTableView<RoleDto> view = new PageTableView<>();
        view.addColumn("ID", "roleId");
        view.addColumn("角色名", "roleName");
        view.addColumn("拥有权限", "authNames", (entity) -> {
            Set<RoleAuthority> roleAuthoritySet = roleAuthorities.stream().filter(roleAuthority -> roleAuthority.getRoleId().equals(entity.getRoleId()))
                    .collect(Collectors.toSet());

            return CollUtil.isEmpty(roleAuthoritySet)
                    ? " - "
                    : roleAuthoritySet.stream().map(RoleAuthority::getAuthorityName).collect(Collectors.joining(","));
        });

        view.addWidget("角色名", "roleName");
        view.addWidget(CrudUtils.getWidget(this, "getAuthorities"));
        //添加默认按钮
        view.addButton(Buttons.defaultButtonsWithMore());
        //添加默认行内操作
//        view.addAction(Buttons.defaultActions());
        return view;
    }

    @Override
    protected Object createEditableView() {
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
