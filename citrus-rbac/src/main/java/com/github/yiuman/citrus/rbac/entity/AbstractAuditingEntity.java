package com.github.yiuman.citrus.rbac.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;

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

    @CreatedBy
    @TableField("created_by")
    private Long createdBy;

    @TableField(value = "created_time",fill = FieldFill.INSERT)
    private LocalDateTime createdTime = LocalDateTime.now();

    @TableField("last_modified_by")
    private Long lastModifiedBy;

    @TableField(value = "last_modified_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime lastModifiedTime = LocalDateTime.now();

    public AbstractAuditingEntity() {
    }
}
