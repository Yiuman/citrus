package com.github.yiuman.citrus.support.crud.view;

import com.github.yiuman.citrus.support.model.Page;

/**
 * 可视化接口
 *
 * @param <T> 数据类型
 * @author yiuman
 * @date 2022/1/23
 */
public interface PageViewable<T> {

    /**
     * 创建视图
     *
     * @param data 数据
     * @return 视图对象
     */
    Object showPageView(Page<T> data);

}