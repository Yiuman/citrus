package com.github.yiuman.citrus.system.authticate;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


/**
 * 登录模型
 *
 * @author yiuman
 * @date 2020/3/26
 */
@Data
@NoArgsConstructor
public class PasswordLoginEntity {

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;

    private String captcha;

}
