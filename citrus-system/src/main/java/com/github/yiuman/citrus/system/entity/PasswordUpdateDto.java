package com.github.yiuman.citrus.system.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 密码更新传输类
 *
 * @author yiuman
 * @date 2020/9/25
 */
@Data
public class PasswordUpdateDto {

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String confirmPassword;
}
