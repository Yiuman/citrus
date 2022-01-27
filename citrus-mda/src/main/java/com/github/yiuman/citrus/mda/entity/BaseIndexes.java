package com.github.yiuman.citrus.mda.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author yiuman
 * @date 2021/4/22
 */
@Data
public abstract class BaseIndexes implements TableRel {

    @TableId(type = IdType.ASSIGN_UUID)
    private String uuid;

    private String indexName;

    private String tableUuid;

    private Boolean uniques;
}
