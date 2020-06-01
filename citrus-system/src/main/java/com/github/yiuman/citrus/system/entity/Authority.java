package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统权限
 *
 * @author yiuman
 * @date 2020/3/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_authority")
public class Authority extends AbstractAuditingEntity{

    @TableId(type = IdType.AUTO)
    private Long authorityId;

    /**
     * 权限名称
     */
    private String authorityName;

    private String describe;

}
