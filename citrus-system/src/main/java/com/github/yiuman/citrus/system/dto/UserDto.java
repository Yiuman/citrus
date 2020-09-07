package com.github.yiuman.citrus.system.dto;

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

    private Long userId;

    private String loginId;

    @NotBlank
    private String username;

    @NotBlank
    private String mobile;

    private String password;

    @NotBlank
    private String email;

    @JsonSerialize(contentUsing = ToStringSerializer.class)
    private List<Long> roleIds;

    /**
     * 所属组织ID
     */
    @JsonSerialize(contentUsing = ToStringSerializer.class)
    private List<Long> organIds;

}
