package com.github.yiuman.citrus.support.crud.query;

import lombok.Builder;
import lombok.Data;

/**
 * 查询条件信息
 *
 * @author yiuman
 * @date 2021/8/15
 */
@Data
@Builder
public class ConditionInfo {

    /**
     * 参数
     */
    private String parameter;

    /**
     * 参数值
     */
    private Object value;

    /**
     * 映射，如实体字段为`userId`映射数据库字段为`user_id`
     */
    private String mapping;

    /**
     * 参数的类型
     */
    private Class<?> type;

    /**
     * 操作符
     */
    private String operator;
}
