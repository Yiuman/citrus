package com.github.yiuman.citrus.mda.meta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 实体元 用于插入或更新操作
 *
 * @author yiuman
 * @date 2021/4/29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveMeta {

    private String namespace;

    private String tableName;

    private Map<String, Object> entity;

}
