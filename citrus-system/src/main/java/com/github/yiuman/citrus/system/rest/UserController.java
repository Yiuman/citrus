package com.github.yiuman.citrus.system.rest;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.security.authorize.Authorize;
import com.github.yiuman.citrus.support.crud.query.Query;
import com.github.yiuman.citrus.support.crud.query.QueryParamHandler;
import com.github.yiuman.citrus.support.crud.query.QueryParamMeta;
import com.github.yiuman.citrus.support.crud.query.annotations.Like;
import com.github.yiuman.citrus.support.crud.query.annotations.QueryParam;
import com.github.yiuman.citrus.support.crud.query.builder.QueryBuilders;
import com.github.yiuman.citrus.support.crud.rest.BaseCrudController;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.crud.view.impl.FormView;
import com.github.yiuman.citrus.support.crud.view.impl.PageTableView;
import com.github.yiuman.citrus.support.exception.RestException;
import com.github.yiuman.citrus.support.http.ResponseEntity;
import com.github.yiuman.citrus.support.http.ResponseStatusCode;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.utils.Buttons;
import com.github.yiuman.citrus.support.utils.CrudUtils;
import com.github.yiuman.citrus.support.widget.Column;
import com.github.yiuman.citrus.support.widget.Inputs;
import com.github.yiuman.citrus.support.widget.Selects;
import com.github.yiuman.citrus.system.dto.PasswordUpdateDto;
import com.github.yiuman.citrus.system.dto.RoleDto;
import com.github.yiuman.citrus.system.dto.UserDto;
import com.github.yiuman.citrus.system.dto.UserOnlineInfo;
import com.github.yiuman.citrus.system.entity.UserOrgan;
import com.github.yiuman.citrus.system.entity.UserRole;
import com.github.yiuman.citrus.system.hook.HasLoginHook;
import com.github.yiuman.citrus.system.inject.AuthDeptIds;
import com.github.yiuman.citrus.system.mapper.UserRoleMapper;
import com.github.yiuman.citrus.system.service.OrganService;
import com.github.yiuman.citrus.system.service.RoleService;
import com.github.yiuman.citrus.system.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
    public Object createPageView() {
        Page<UserDto> page = getPageViewData();
        List<UserDto> records = page.getRecords();
        //找到关联
        Set<Long> userIds = records.stream().map(UserDto::getUserId).collect(Collectors.toSet());
        List<UserRole> userRoles = userService.getUserRolesByUserIds(userIds);
        List<UserOrgan> userOrgans = userService.getUserOrgansByUserIds(userIds);

        PageTableView<UserDto> view = new PageTableView<>();
        view.addColumn("ID", "userId").align(Column.Align.start);
        view.addColumn("用户名", "username", true);
        view.addColumn("手机号码", "mobile");
        view.addColumn("邮箱", "email");
        view.addColumn("所属角色", (entity) -> userRoles.stream()
                .filter(userRole -> userRole.getUserId().equals(entity.getUserId()))
                .map(UserRole::getRoleName).filter(Objects::nonNull).collect(Collectors.joining(",")));
        view.addColumn("所属机构", (entity) -> userOrgans.stream().filter(userOrgan -> userOrgan.getUserId().equals(entity.getUserId()))
                .map(UserOrgan::getOrganName).filter(Objects::nonNull).collect(Collectors.joining(",")));
        view.addWidget(new Inputs("用户名", "username"));
        //添加默认按钮
        view.addButton(Buttons.defaultButtonsWithMore());
        //添加默认行内操作
//        view.addAction(Buttons.defaultActions());
        return view;
    }

    @Override
    public FormView createFormView() {
        FormView dialogView = new FormView();
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
    public ResponseEntity<Void> updatePassword(@Validated @RequestBody PasswordUpdateDto passwordUpdate) throws Exception {
        userService.updatePassword(passwordUpdate.getOldPassword(), passwordUpdate.getNewPassword());
        return ResponseEntity.ok();
    }

    @Data
    static class UserQuery {

        @Like
        private String username;

        @QueryParam(handler = UserQueryHandler.class)
        private List<Long> roleIds;

        @AuthDeptIds
        private Set<Long> deptIds;

        @Component
        public static class UserQueryHandler implements QueryParamHandler {

            private final UserRoleMapper userRoleMapper;

            UserQueryHandler(UserRoleMapper userRoleMapper) {
                this.userRoleMapper = userRoleMapper;
            }

            @SuppressWarnings("unchecked")
            @Override
            public void handle(QueryParamMeta queryParamMeta, Object object, Query query) {
                Field field = queryParamMeta.getField();
                ReflectionUtils.makeAccessible(field);
                List<Long> roleIds = (List<Long>) ReflectionUtils.getField(field, object);
                if (roleIds == null) {
                    return;
                }
                List<Long> userRoles = userRoleMapper.selectList(Wrappers.<UserRole>lambdaQuery().in(UserRole::getRoleId, roleIds))
                        .stream()
                        .map(UserRole::getUserId)
                        .collect(Collectors.toList());

                if (CollectionUtils.isEmpty(userRoles)) {
                    userRoles = Collections.singletonList(0L);

                }
                QueryBuilders.wrapper(query).in("user_id", userRoles);
            }
        }

    }

}
