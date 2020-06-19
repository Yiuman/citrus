package com.github.yiuman.citrus.system.rest;

import com.github.yiuman.citrus.security.authorize.Authorize;
import com.github.yiuman.citrus.support.crud.rest.BaseCrudController;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.model.DialogView;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.utils.Buttons;
import com.github.yiuman.citrus.support.utils.CrudUtils;
import com.github.yiuman.citrus.support.widget.Inputs;
import com.github.yiuman.citrus.support.widget.Selects;
import com.github.yiuman.citrus.system.dto.RoleDto;
import com.github.yiuman.citrus.system.dto.UserDto;
import com.github.yiuman.citrus.system.dto.UserQuery;
import com.github.yiuman.citrus.system.entity.Organization;
import com.github.yiuman.citrus.system.entity.Role;
import com.github.yiuman.citrus.system.hook.HasLoginHook;
import com.github.yiuman.citrus.system.service.OrganService;
import com.github.yiuman.citrus.system.service.RoleService;
import com.github.yiuman.citrus.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户控制层
 *
 * @author yiuman
 * @date 2020/3/30
 */
@RestController
@RequestMapping("/rest/users")
@Authorize(HasLoginHook.class)
@Slf4j
public class UserController extends BaseCrudController<UserDto, Long> {

    private final UserService userService;

    private final RoleService roleService;

    private final OrganService organService;

    public UserController(UserService userService, RoleService roleService, OrganService organService) {
        this.userService = userService;
        this.roleService = roleService;
        this.organService = organService;
        setParamClass(UserQuery.class);
    }

    @Override
    protected CrudService<UserDto, Long> getService() {
        return userService;
    }

    @Override
    protected Page<UserDto> createPage() throws Exception {
        Page<UserDto> page = super.createPage();
        page.addHeader("ID", "userId");
        page.addHeader("用户名", "username", true);
        page.addHeader("手机号码", "mobile");
        page.addHeader("邮箱", "email");
        page.addHeader("所属角色", "roleNames", (entity) -> {
            List<Role> roleByUser = userService.getRoleByUser(entity);
            entity.setRoleIds(roleByUser.parallelStream().map(Role::getRoleId).collect(Collectors.toList()));
            return roleByUser.parallelStream().map(Role::getRoleName).collect(Collectors.joining(","));
        });

        page.addHeader("所属机构", "organNames", (entity) -> {
            List<Organization> organByUser = userService.getOrganByUser(entity.getUserId());
            entity.setOrganIds(organByUser.parallelStream().map(Organization::getOrganId).collect(Collectors.toList()));
            return organByUser.parallelStream().map(Organization::getOrganName).collect(Collectors.joining(","));
        });

        page.addWidget(new Inputs("用户名", "username"));
        //添加默认按钮
        page.addButton(Buttons.defaultButtons());
        //添加默认行内操作
        page.addActions(Buttons.defaultActions());
        return page;
    }

    @Override
    protected DialogView createDialogView() throws Exception {
        DialogView dialogView = new DialogView();
        dialogView.addEditField("登录名", "loginId").addRule("required");
        dialogView.addEditField("用户名", "username").addRule("required");
        dialogView.addEditField("密码", "password").addRule("required");
        dialogView.addEditField("手机号码", "mobile").addRule("required","phone");
        dialogView.addEditField("邮箱", "email");
        dialogView.addEditField("选择角色", "roleIds", CrudUtils.getWidget(this, "getRoleSelects"));
        dialogView.addEditField("选择机构", "organIds", organService.getOrganTree("选择机构", "organIds", true));
        return dialogView;
    }

    @Selects(bind = "roleIds", key = "roleId", label = "roleName", text = "所属角色", multiple = true)
    public List<RoleDto> getRoleSelects() {
        return roleService.list();
    }


}
