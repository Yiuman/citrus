package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 权限资源及数据范围的关联关系
 * 定义对某个权限的作用范围
 *
 * @author yiuman
 * @date 2020/3/30
 */
@Data
@TableName("sys_auth_scope")
public class AuthorityScope {

    /**
     * 数据范围的ID
     * @see Scope
     */
    @TableId
    private Long scopeId;

    /**
     * 权限ID
     * @see Authority
     */
    @TableId
    private Long authId;

    /**
     * 数据范围对应的资源
     * @see Resource
     */
    @TableId
    private Long resourceId;

}
