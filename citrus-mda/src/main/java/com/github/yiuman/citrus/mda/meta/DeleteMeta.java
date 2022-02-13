package com.github.yiuman.citrus.mda.meta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 删除数据元对象
 *
 * @author yiuman
 * @date 2021/5/1
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteMeta {

    private String namespace;

    private String tableName;

}
