package com.github.yiuman.citrus.support.widget;

import java.util.Set;

/**
 * 拥有属性的对象
 *
 * @param <T> 拥有属性的对象本身
 * @author camel
 * @date 2021-12-31
 */
public interface Propertied<T extends Propertied<T>> {
    /**
     * 设置一个属性
     *
     * @param name  属性名
     * @param value 属性值
     * @return 返回对象本身，用于链式编程
     */
    T setProperty(String name, Object value);

    /**
     * 获得一个属性
     *
     * @param name 属性名
     * @return 属性值
     * @throws Exception 获得数据异常
     */
    Object getProperty(String name) throws Exception;

    /**
     * 判断是否包含某个属性
     *
     * @param name 属性名
     * @return 包含此属性返回true，否则返回false
     */
    boolean containsProperty(String name);

    /**
     * 获得所有属性的名称
     *
     * @return 所有属性的名称的集合，如果没有定义任何属性，返回一个空集合
     */
    Set<String> getProperties();
}