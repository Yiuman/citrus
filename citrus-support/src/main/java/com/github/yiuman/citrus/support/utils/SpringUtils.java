package com.github.yiuman.citrus.support.utils;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * Spring相关工具
 *
 * @author yiuman
 * @date 2020/4/4
 */
@Component
public class SpringUtils extends SpringUtil {


    public static <T> T getBean(Class<T> clazz) {
        return getBean(clazz, false);
    }

    public static <T> T getBean(Class<T> clazz, boolean force) {
        T bean;
        try {
            bean = getApplicationContext().getBean(clazz);
        } catch (NoSuchBeanDefinitionException ex) {
            bean = force ? getApplicationContext().getAutowireCapableBeanFactory().createBean(clazz) : null;
        }
        return bean;
    }

    public static <T> T getBean(Class<T> tClass, String name) {
        return getApplicationContext().getBean(name, tClass);
    }

    public static <T> T getBean(Class<T> clazz, String name, boolean force) {
        T bean;
        try {
            bean = getApplicationContext().getBean(name, clazz);
        } catch (NoSuchBeanDefinitionException ex) {
            bean = force ? getApplicationContext().getAutowireCapableBeanFactory().createBean(clazz) : null;
        }
        return bean;
    }

    public static <T> Map<String, T> getBeanOfType(Class<T> type) {
        return getApplicationContext().getBeansOfType(type);
    }


    @SuppressWarnings("unchecked")
    public static <PROXY extends T, T> PROXY getProxy(T object) {
        Object proxy = null;
        try {
            proxy = AopContext.currentProxy();
        } catch (IllegalStateException ignore) {
        }
        return Objects.nonNull(proxy) ? (PROXY) proxy : (PROXY) object;
    }

}
