package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 权限与资源的映射
 *
 * @author yiuman
 * @date 2020/3/30
 */
@Data
@TableName("sys_auth_resource")
@AllArgsConstructor
public class AuthorityResource {

    private Long authorityId;

    private Long resourceId;
}
