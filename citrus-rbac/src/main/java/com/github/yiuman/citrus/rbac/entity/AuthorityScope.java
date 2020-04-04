package com.github.yiuman.citrus.rbac.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 数据范围
 * 定义某个角色对某个权限的作用范围
 *
 * @author yiuman
 * @date 2020/3/30
 */
@Data
@TableName("sys_auth_scope")
public class AuthorityScope {

    @TableId(type = IdType.AUTO)
    private Integer scopeId;

    /**
     * 范围名称
     */
    private String scopeName;

    /**
     * 所属组织
     */
    private Integer organId;

}
