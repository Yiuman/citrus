package com.github.yiuman.citrus.rbac.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 权限与资源的映射
 *
 * @author yiuman
 * @date 2020/3/30
 */
@Data
@TableName("sys_auth_resource")
public class AuthorityResource {

    private Integer authorityId;

    private Integer resourceId;
}
