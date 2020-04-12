package com.github.yiuman.citrus.system.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Set;

/**
 * 权限dto
 *
 * @author yiuman
 * @date 2020/4/11
 */
@Data
public class AuthorityDto {

    private Long authorityId;

    /**
     * 权限名称
     */
    private String authorityName;

    private Integer scopeId;

    private String describe;

    /**
     * 资源列表
     */
    private Set<Long> resourceIds;
}
