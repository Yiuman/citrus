package com.github.yiuman.citrus.support.inject;

import java.lang.annotation.Annotation;

/**
 * 注解解析器持有者
 *
 * @author yiuman
 * @date 2020/7/23
 */
public interface InjectAnnotationParserHolder extends TargetAnnotationInjector {

    /**
     * 获取注解的解析器
     *
     * @param annotationClass 注解类型
     * @param <T>             注解
     * @return 注解解析器
     */
    <T extends Annotation> InjectAnnotationParser<T> getParse(Class<T> annotationClass);

    /**
     * 注册解释器
     *
     * @param parser 注解解析器
     */
    void register(InjectAnnotationParser<?> parser);

    /**
     * 根据注解类型解析返回目标
     *
     * @param annotation 注解
     * @param <A>        注解类型
     * @return 目标信息
     */
    <A extends Annotation> Object parse(A annotation);

}