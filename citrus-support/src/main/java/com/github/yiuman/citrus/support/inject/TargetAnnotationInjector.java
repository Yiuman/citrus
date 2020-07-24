package com.github.yiuman.citrus.support.inject;

/**
 * 注入者
 *
 * @author yiuman
 * @date 2020/7/23
 */
public interface TargetAnnotationInjector {

    /**
     * 将值注入目标对象
     * @param target 注入的目标对象
     */
    void inject(Object target);
}