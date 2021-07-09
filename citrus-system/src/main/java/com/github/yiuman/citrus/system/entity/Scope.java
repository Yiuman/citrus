package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 数据范围
 *
 * @author yiuman
 * @date 2020/5/29
 */
@Data
@TableName("sys_scope")
public class Scope {

    @TableId(type = IdType.ASSIGN_ID)
    private Long scopeId;

    private String scopeName;

    /**
     * 数据范围所属组织,通用的为-1
     *
     * @see Organization
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long organId;

}
