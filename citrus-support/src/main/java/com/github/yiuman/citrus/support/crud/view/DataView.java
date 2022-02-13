package com.github.yiuman.citrus.support.crud.view;

/**
 * 数据视图
 *
 * @param <DATA> 数据类型
 * @author yiuman
 * @date 2022/2/11
 */
public interface DataView<DATA> {

    /**
     * 获取数据
     *
     * @return data
     */
    DATA getData();

    /**
     * 设置数据
     *
     * @param data 数据
     */
    void setData(DATA data);
}