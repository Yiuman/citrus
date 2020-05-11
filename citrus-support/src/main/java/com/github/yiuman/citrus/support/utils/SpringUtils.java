package com.github.yiuman.citrus.support.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

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
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (context == null) {
            context = applicationContext;
        }
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static <T> T getBean(Class<T> tClass) {
        T bean;
        try {
            bean=context.getBean(tClass);
        }catch (NoSuchBeanDefinitionException ex){
            bean = context.getAutowireCapableBeanFactory().createBean(tClass);
        }
        return bean;
    }

    public static <T> T getBean(Class<T> tClass, String name) {
        return context.getBean(name, tClass);
    }

}
