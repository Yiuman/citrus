package com.github.yiuman.citrus.mda.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import com.github.yiuman.citrus.support.crud.AbstractAuditingEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yiuman
 * @date 2021/4/27
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class BaseTable extends AbstractAuditingEntity {

    @TableId(type = IdType.ASSIGN_UUID)
    private String uuid;

    private String namespace;

    private String tableName;

    /**
     * 主键策略
     */
    private IdType idType;

    private String comments;

    /**
     * 版本号
     */
    @Version
    private Integer version;

    public BaseTable() {
    }
}
