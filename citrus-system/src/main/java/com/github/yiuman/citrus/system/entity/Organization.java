package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.yiuman.citrus.support.model.BaseTree;
import lombok.Data;

/**
 * 组织（部门）
 *
 * @author yiuman
 * @date 2020/3/30
 */
@Data
@TableName("sys_organ")
public class Organization extends BaseTree<Organization, Long> {

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

    private Integer leftValue;

    private Integer rightValue;

    private Integer deep;

    /**
     * 描述说明
     */
    private String remark;

    @Override
    public Long getId() {
        return organId;
    }

    public Organization() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Organization)) return false;

        Organization that = (Organization) o;

        return organId != null ? organId.equals(that.organId) : that.organId == null;
    }

    @Override
    public int hashCode() {
        return organId != null ? organId.hashCode() : 0;
    }
}
