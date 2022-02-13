package com.github.yiuman.citrus.mda.ddl;

import org.apache.ibatis.session.SqlSessionFactory;

/**
 * 元数据的上下文
 *
 * @param <T> 元数据类型
 * @author yiuman
 * @date 2021/4/25
 */
public interface MetadataContext<T> {

    /**
     * 元数据信息
     *
     * @return 元数据信息实体
     */
    T getMetadata();

    /**
     * 获取对应的SqlSessionFactory
     * 与namespace对应
     *
     * @return SqlSessionFactory
     */
    SqlSessionFactory getSqlSessionFactory();


}