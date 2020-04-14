package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户与组织机构映射
 *
 * @author yiuman
 * @date 2020/4/11
 */
@Data
@TableName("sys_user_organ")
public class UserOrgan {

    private Long userId;

    private User user;

    private Long organId;

    private Organization organ;

    public UserOrgan(Long userId, Long organId) {
        this.userId = userId;
        this.organId = organId;
    }
}
