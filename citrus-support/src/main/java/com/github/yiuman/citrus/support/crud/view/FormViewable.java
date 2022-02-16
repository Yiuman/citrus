package com.github.yiuman.citrus.support.crud.view;

/**
 * 可表单化视图
 *
 * @author yiuman
 * @date 2022/2/14
 */
public interface FormViewable {

    /**
     * 创建表单视图
     *
     * @return 表单
     */
    Object createFormView();
}