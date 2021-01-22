package com.github.yiuman.citrus.system.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 用户Dto
 *
 * @author yiuman
 * @date 2020/3/31
 */
@Data
public class UserDto {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    private String loginId;

    @NotBlank
    private String username;

    @NotBlank
    private String mobile;

    @ExcelIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String email;

    @ExcelIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String avatar;

    @JsonSerialize(contentUsing = ToStringSerializer.class)
    private List<Long> roleIds;

    /**
     * 所属组织ID
     */
    @JsonSerialize(contentUsing = ToStringSerializer.class)
    private List<Long> organIds;

    private String uuid;

    private Integer version;

}
