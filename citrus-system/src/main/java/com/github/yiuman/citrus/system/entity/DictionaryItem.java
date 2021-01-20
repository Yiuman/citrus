package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 字典项
 *
 * @author yiuman
 * @date 2020/4/8
 */
@Data
public class DictionaryItem {

    /**
     * 主键
     */
    @TableId
    private Long itemId;

    /**
     * 字典项名称
     */
    private String itemName;

    /**
     * 字典项代码
     */
    private String itemCode;

    /**
     * 排序
     */
    private Integer orderId;

    /**
     * 所属字典
     */
    private Long dictId;

}
