package com.github.yiuman.citrus.mda.ddl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * @param <T> 元数据类型
 * @author yiuman
 * @date 2021/4/26
 */
@Data
@Builder
@AllArgsConstructor
public class MetadataContextImpl<T> implements MetadataContext<T> {

    private T metadata;

    private SqlSessionFactory sqlSessionFactory;

}
