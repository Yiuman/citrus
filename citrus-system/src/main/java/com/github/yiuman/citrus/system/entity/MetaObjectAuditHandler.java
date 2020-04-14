package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.github.yiuman.citrus.system.service.UserService;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 实体审计处理器
 *
 * @author yiuman
 * @date 2020/4/14
 */
@Component
public class MetaObjectAuditHandler implements MetaObjectHandler {

    private final UserService userService;

    public MetaObjectAuditHandler(UserService userService) {
        this.userService = userService;
    }

    private String getOperationUser() {
        Optional<User> user = userService.getUser(SecurityContextHolder.getContext().getAuthentication());
        return user.isPresent() ? "SYSTEM" : user.get().getUsername();
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createdTime", LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐使用)
        this.strictInsertFill(metaObject, "createBy", String.class, getOperationUser());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "lastModifiedTime", LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐使用)
        this.strictUpdateFill(metaObject, "lastModifiedBy", String.class, getOperationUser());

    }
}
