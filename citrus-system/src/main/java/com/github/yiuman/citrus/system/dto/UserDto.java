package com.github.yiuman.citrus.system.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @NotBlank
    private String username;

    private String age;

    @NotBlank
    private String mobile;

    @NotBlank
    @JsonIgnore
    private String password;

    @NotBlank
    private String email;

    private List<Long> roleIds;

    /**
     * 所属组织ID
     */
    private List<Long> organIds;

}
