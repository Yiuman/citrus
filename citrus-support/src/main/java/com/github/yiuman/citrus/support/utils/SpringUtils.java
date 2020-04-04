package com.github.yiuman.citrus.support.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletRequestAttributes;

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

    public static  <T> T getBean(Class<T> tClass) {
        return context.getBean(tClass);
    }

    public static <T> T getBean(Class<T> tClass, String name) {
        return context.getBean(name, tClass);
    }

}
