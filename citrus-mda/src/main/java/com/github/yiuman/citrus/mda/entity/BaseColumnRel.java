package com.github.yiuman.citrus.mda.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 需要关联字段实体的基础信息
 *
 * @author yiuman
 * @date 2021/4/20
 */
@Data
public abstract class BaseColumnRel implements TableRel {

    @TableId(type = IdType.ASSIGN_UUID)
    private String uuid;

    private String tableUuid;

    private String columnUuid;

}
