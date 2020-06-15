package com.github.yiuman.citrus.system.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 角色实体传输类
 *
 * @author yiuman
 * @date 2020/4/6
 */
@Data
public class RoleDto {

    private Long roleId;

    /**
     * 角色名
     */
    @NotBlank
    private String roleName;

    /**
     * 父角色Id
     */
    private Integer parentId;

    /**
     * 0：角色 1：角色组（）
     */
    private Integer type = 0;

    /**
     * 排序ID
     */
    private Integer orderId;

    /**
     * 权限ID
     */
    @JsonSerialize(contentUsing = ToStringSerializer.class)
    private List<Long> authIds;

}
