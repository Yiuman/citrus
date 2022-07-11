package com.github.yiuman.citrus.support.utils;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;
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
public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    public SpringUtils() {
    }

    @Override
    public void setApplicationContext(@Nullable ApplicationContext applicationContext) throws BeansException {
        if (context == null) {
            context = applicationContext;
        }
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static <T> T getBean(Class<T> clazz) {
        return getBean(clazz, false);
    }

    public static <T> T getBean(Class<T> clazz, boolean force) {
        T bean;
        try {
            bean = context.getBean(clazz);
        } catch (NoSuchBeanDefinitionException ex) {
            bean = force ? context.getAutowireCapableBeanFactory().createBean(clazz) : null;
        }
        return bean;
    }

    public static <T> T getBean(Class<T> tClass, String name) {
        return context.getBean(name, tClass);
    }

    public static <T> T getBean(Class<T> clazz, String name, boolean force) {
        T bean;
        try {
            bean = context.getBean(name, clazz);
        } catch (NoSuchBeanDefinitionException ex) {
            bean = force ? context.getAutowireCapableBeanFactory().createBean(clazz) : null;
        }
        return bean;
    }

    public static <T> Map<String, T> getBeanOfType(Class<T> type) {
        return context.getBeansOfType(type);
    }


    @SuppressWarnings("unchecked")
    public static  <PROXY extends T, T> PROXY getProxy(T object) {
        Object proxy = null;
        try {
            proxy = AopContext.currentProxy();
        } catch (IllegalStateException ignore) {
        }
        return Objects.nonNull(proxy) ? (PROXY) proxy : (PROXY) object;
    }


}
