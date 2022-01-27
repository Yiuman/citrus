package com.github.yiuman.citrus.mda.entity;

/**
 * 与表实体有关联的接口
 *
 * @author yiuman
 * @date 2021/4/20
 */
public interface TableRel {
    /**
     * 获取关联的表实体的主键信息
     *
     * @return 表主键
     */
    String getTableUuid();

    /**
     * 设置表实体的UUID
     *
     * @param tableUuid 表实体主键uuid
     */
    void setTableUuid(String tableUuid);
}