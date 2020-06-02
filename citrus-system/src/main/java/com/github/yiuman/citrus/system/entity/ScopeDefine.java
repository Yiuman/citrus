package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.yiuman.citrus.system.enums.ScopeType;
import lombok.Data;
import org.apache.ibatis.type.ArrayTypeHandler;

/**
 * 数据范围的定义，是排除部门或者包含子部门等
 * 数据范围与部门的关联关系
 *
 * @author yiuman
 * @date 2020/5/28
 */
@Data
@TableName("sys_scope_define")
public class ScopeDefine {

    @TableId
    private Long id;

    /**
     * 关联的数据范围对象ID
     *
     * @see AuthorityScope
     */
    private Long scopeId;

    /**
     * 组织机构的ID  >0表示对应某个组织，-1表示当前用户部门
     * @see Organization
     */
    private Long organId;

    /**
     * 数据范围规则
     * 0 包含 1 排除
     */
    private Integer scopeRule;

    /**
     * 数据范围的类型
     */
    @TableField(typeHandler = ArrayTypeHandler.class)
    private ScopeType[] scopeTypes;

}