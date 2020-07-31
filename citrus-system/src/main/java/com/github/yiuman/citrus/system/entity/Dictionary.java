package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 字典
 *
 * @author yiuman
 * @date 2020/4/8
 */
@Data
@TableName("sys_dict")
public class Dictionary {

    @TableId(type = IdType.ASSIGN_ID)
    private Long dictId;

    private String dictCode;

    private String dictName;

}
