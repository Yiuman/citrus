package com.github.yiuman.citrus.support.inject;

import java.lang.annotation.Annotation;

/**
 * 注解解析器持有者
 *
 * @author yiuman
 * @date 2020/7/23
 */
public interface InjectAnnotationParserHolder extends TargetAnnotationInjector{

    <T extends Annotation> InjectAnnotationParser<T> getParse(Class<T> annotationClass);

    void register(InjectAnnotationParser<?> parser);

    <A extends Annotation>Object parse(A annotation);

}