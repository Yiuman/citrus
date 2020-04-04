package com.github.yiuman.citrus.rbac.dto;

import lombok.Data;

/**
 * 用户Dto
 * @author yiuman
 * @date 2020/3/31
 */
@Data
public class UserDto {

    private Long userId;

    private String userName;

    private String age;

    private String mobile;

    private String password;

    private String email;
}
