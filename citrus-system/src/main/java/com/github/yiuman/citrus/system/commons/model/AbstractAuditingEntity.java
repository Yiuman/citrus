package com.github.yiuman.citrus.system.commons.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 审计实体
 *
 * @author yiuman
 * @date 2020/3/30
 */
@Data
public class AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private Long createdBy;

    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime = LocalDateTime.now();

    @TableField(value = "last_modified_by", fill = FieldFill.INSERT_UPDATE)
    private Long lastModifiedBy;

    @TableField(value = "last_modified_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime lastModifiedTime = LocalDateTime.now();

    public AbstractAuditingEntity() {
    }
}
