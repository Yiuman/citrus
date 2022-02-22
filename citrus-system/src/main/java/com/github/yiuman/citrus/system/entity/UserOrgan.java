package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 用户与组织机构映射
 *
 * @author yiuman
 * @date 2020/4/11
 */
@Data
@TableName("sys_user_organ")
@NoArgsConstructor
public class UserOrgan {

    @TableId
    @TableField(exist = false)
    private String id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @TableField(exist = false)
    private User user;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long organId;

    @TableField(exist = false)
    private Organization organ;

    public UserOrgan(Long userId, Long organId) {
        this.userId = userId;
        this.organId = organId;
    }

    public String getId() {
        return String.format("%s-%s", userId, organId);
    }

    public String getOrganName() {
        return Objects.nonNull(organ) ? organ.getOrganName() : null;
    }
}
