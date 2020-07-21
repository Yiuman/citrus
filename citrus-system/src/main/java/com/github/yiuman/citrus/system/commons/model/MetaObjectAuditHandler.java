package com.github.yiuman.citrus.system.commons.model;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.github.yiuman.citrus.support.utils.SpringUtils;
import com.github.yiuman.citrus.system.entity.User;
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

    /**
     * 获取操作人
     */
    private Long getOperationUser() {
        Optional<User> user = SpringUtils.getBean(UserService.class).getUser(SecurityContextHolder.getContext().getAuthentication());
        return user.isPresent() ? user.get().getUserId() : -1;
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createdTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "createdBy", Long.class, getOperationUser());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "lastModifiedTime", LocalDateTime.class, LocalDateTime.now());
        this.strictUpdateFill(metaObject, "lastModifiedBy", Long.class, getOperationUser());

    }
}
