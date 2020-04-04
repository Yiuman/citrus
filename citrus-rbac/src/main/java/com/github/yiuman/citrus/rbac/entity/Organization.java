package com.github.yiuman.citrus.rbac.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 组织（部门）
 *
 * @author yiuman
 * @date 2020/3/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_organ")
public class Organization extends AbstractAuditingEntity {

    @TableId(type = IdType.AUTO)
    private Integer organId;

    /**
     * 组织名
     */
    private String organName;

    /**
     * 上级ID
     */
    private Integer parentId;

    private Integer leftValue;

    private Integer rightValue;

}
