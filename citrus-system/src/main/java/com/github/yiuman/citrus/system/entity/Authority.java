package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
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

    @JsonSerialize(using = StringSerializer.class)
    @TableId(type = IdType.ASSIGN_ID)
    private Long authorityId;

    /**
     * 权限名称
     */
    private String authorityName;

    private String remark;

}
