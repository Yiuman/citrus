package com.github.yiuman.citrus.elasticsearch.utils;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.TypeUtil;
import com.github.yiuman.citrus.support.utils.JavassistUtils;
import com.github.yiuman.citrus.support.utils.SpringUtils;
import javassist.CtClass;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchRepositoryFactory;

import java.io.Serializable;

/**
 * ES工具类
 *
 * @author yiuman
 * @date 2021/8/2
 */
@Slf4j
public final class ElasticsearchUtils {

    private ElasticsearchUtils() {
    }

    @SuppressWarnings("unchecked")
    public static synchronized <E, K extends Serializable> Class<? extends ElasticsearchRepository<E, K>>
    getElasticsearchRepositoryInterface(Class<E> entityClass, Class<K> keyClass) throws Exception {
        String entityClassName = entityClass.getName();
        String formatName = String.format("%sElasticsearchRepository$$javassist", entityClassName);
        Class<?> elasticsearchRepositoryInterface;
        try {
            elasticsearchRepositoryInterface = JavassistUtils.defaultPool().getClassLoader().loadClass(formatName);
        } catch (ClassNotFoundException e) {
            CtClass ctClass;
            try {
                ctClass = JavassistUtils.defaultPool().getCtClass(formatName);
                elasticsearchRepositoryInterface = ctClass.getClass();
            } catch (NotFoundException notFoundCtClass) {
                ctClass = JavassistUtils.defaultPool().makeClass(formatName, JavassistUtils.getClass(ElasticsearchRepository.class));
                JavassistUtils.addTypeArgument(ctClass, ElasticsearchRepository.class, new Class[]{entityClass, keyClass}, null, null);
                elasticsearchRepositoryInterface = ctClass.toClass();
            }
        }
        return (Class<? extends ElasticsearchRepository<E, K>>) elasticsearchRepositoryInterface;
    }

    public static <E, K extends Serializable, T extends ElasticsearchRepository<E, K>> T getRepository(Class<E> entityClass, Class<K> keyClass) {
        try {
            ElasticsearchRepositoryFactory elasticsearchRepositoryFactory = SpringUtils.getBean(ElasticsearchRepositoryFactory.class);
            Assert.notNull(elasticsearchRepositoryFactory, "cannot found ElasticsearchRepositoryFactory bean in Spring context");
            return elasticsearchRepositoryFactory.getRepository(getElasticsearchRepositoryInterface(entityClass, keyClass));
        } catch (Throwable throwable) {
            log.error("Cannot auto create baseService for entity {}", TypeUtil.getTypeArgument(entityClass, 0), throwable);
            throw new RuntimeException(throwable);
        }

    }

}
