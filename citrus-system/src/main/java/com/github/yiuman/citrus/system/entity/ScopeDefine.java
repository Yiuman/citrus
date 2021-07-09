package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.yiuman.citrus.support.crud.EnumArrayHandler;
import com.github.yiuman.citrus.system.enums.ScopeType;
import lombok.Data;

/**
 * 数据范围的定义，是排除部门或者包含子部门等
 * 数据范围与部门的关联关系
 *
 * @author yiuman
 * @date 2020/5/28
 */
@Data
@TableName(value = "sys_scope_define", autoResultMap = true)
public class ScopeDefine {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId
    private Long id;

    /**
     * 关联的数据范围对象ID
     *
     * @see Scope
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long scopeId;

    /**
     * 组织机构的ID
     * >0表示对应某个组织
     * 0表示当前用户部门
     * -1表示一级部门
     * -2表示二级部门
     * 以此类推，这里用组织机构树的deep对应
     *
     * @see Organization
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long organId;

    /**
     * 数据范围规则
     * 0 包含 1 排除
     */
    private Integer scopeRule;

    /**
     * 数据范围的类型
     */
    @TableField(typeHandler = EnumArrayHandler.class)
    private ScopeType[] scopeTypes;

}
