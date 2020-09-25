package com.github.yiuman.citrus.system.rest;

import com.github.yiuman.citrus.security.authorize.Authorize;
import com.github.yiuman.citrus.support.crud.rest.BaseCrudController;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.exception.RestException;
import com.github.yiuman.citrus.support.http.ResponseEntity;
import com.github.yiuman.citrus.support.http.ResponseStatusCode;
import com.github.yiuman.citrus.support.model.DialogView;
import com.github.yiuman.citrus.support.model.Header;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.utils.Buttons;
import com.github.yiuman.citrus.support.utils.CrudUtils;
import com.github.yiuman.citrus.support.widget.Inputs;
import com.github.yiuman.citrus.support.widget.Selects;
import com.github.yiuman.citrus.system.dto.RoleDto;
import com.github.yiuman.citrus.system.dto.UserDto;
import com.github.yiuman.citrus.system.dto.UserOnlineInfo;
import com.github.yiuman.citrus.system.dto.UserQuery;
import com.github.yiuman.citrus.system.entity.Organization;
import com.github.yiuman.citrus.system.entity.PasswordUpdateDto;
import com.github.yiuman.citrus.system.entity.Role;
import com.github.yiuman.citrus.system.hook.HasLoginHook;
import com.github.yiuman.citrus.system.service.OrganService;
import com.github.yiuman.citrus.system.service.RoleService;
import com.github.yiuman.citrus.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        page.addHeader("ID", "userId").setAlign(Header.Align.start);
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
        page.addButton(Buttons.defaultButtonsWithMore());
        //添加默认行内操作
        page.addActions(Buttons.defaultActions());
        return page;
    }

    @Override
    protected DialogView createDialogView() throws Exception {
        DialogView dialogView = new DialogView();
        dialogView.addEditField("登录名", "loginId").addRule("required");
        dialogView.addEditField("用户名", "username").addRule("required");
        dialogView.addEditField("手机号码", "mobile").addRule("required", "phone");
        dialogView.addEditField("邮箱", "email");
        dialogView.addEditField("选择角色", "roleIds", CrudUtils.getWidget(this, "getRoleSelects"));
        dialogView.addEditField("选择机构", "organIds", organService.getOrganTree("选择机构", "organIds", true));
        return dialogView;
    }

    @Selects(bind = "roleIds", key = "roleId", label = "roleName", text = "所属角色", multiple = true)
    public List<RoleDto> getRoleSelects() {
        return roleService.list();
    }

    /**
     * 获取当前用户
     *
     * @return 当前用户实例
     */
    @GetMapping("/current")
    public ResponseEntity<UserOnlineInfo> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUserOnlineInfo());
    }


    /**
     * 修改个人信息
     *
     * @param entity 用户实体
     * @return Void
     */
    @PostMapping("/profile")
    @Authorize(HasLoginHook.class)
    public ResponseEntity<Void> saveProfile(@Validated @RequestBody UserDto entity) {
        UserOnlineInfo currentUserOnlineInfo = userService.getCurrentUserOnlineInfo();
        if (!entity.getUserId().equals(currentUserOnlineInfo.getUserId())) {
            throw new RestException("You cannot modify non-personal data", ResponseStatusCode.BAD_REQUEST);
        }

        userService.saveProfile(entity);

        return ResponseEntity.ok();
    }

    /**
     * 修改密码
     *
     * @param passwordUpdate 密码更新传输类
     * @return Void
     * @throws Exception 数据库操作异常
     */
    @PostMapping("/password")
    @Authorize(HasLoginHook.class)
    public ResponseEntity<Void> updatePassword(@Validated @RequestBody PasswordUpdateDto passwordUpdate) throws Exception {
        userService.updatePassword(passwordUpdate.getOldPassword(), passwordUpdate.getNewPassword());
        return ResponseEntity.ok();
    }

}
