package com.github.yiuman.citrus.rbac.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户Dto
 *
 * @author yiuman
 * @date 2020/3/31
 */
@Data
public class UserDto {

    private Long userId;

    @NotBlank
    private String username;

    private String age;

    @NotBlank
    private String mobile;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

}
