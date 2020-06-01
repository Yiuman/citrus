package com.github.yiuman.citrus.system.dto;

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

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    private List<Long> roleIds;

    /**
     * 所属组织ID
     */
    private List<Long> organIds;

}
