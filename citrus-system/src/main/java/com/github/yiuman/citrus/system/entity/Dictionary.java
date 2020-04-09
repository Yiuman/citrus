package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 字典
 *
 * @author yiuman
 * @date 2020/4/8
 */
@Data
public class Dictionary {

    @TableId
    private Long dictId;

    private String dictCode;

    private String dictName;

}
