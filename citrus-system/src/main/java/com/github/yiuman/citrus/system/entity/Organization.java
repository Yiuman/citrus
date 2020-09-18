package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.yiuman.citrus.support.model.BasePreOrderTree;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 组织（部门）
 *
 * @author yiuman
 * @date 2020/3/30
 */
@Data
@TableName("sys_organ")
@EqualsAndHashCode(of = {"organId"}, callSuper = false)
public class Organization extends BasePreOrderTree<Organization, Long> {

    @TableId(type = IdType.AUTO)
    private Long organId;

    /**
     * 组织名
     */
    private String organName;

    /**
     * 组织代码
     */
    private String organCode;

    /**
     * 上级ID
     */
    private Long parentId;

    /**
     * 描述说明
     */
    private String remark;


    @Override
    @JsonSerialize(using = ToStringSerializer.class)
    public Long getId() {
        return organId;
    }

    public Organization() {
    }
}
