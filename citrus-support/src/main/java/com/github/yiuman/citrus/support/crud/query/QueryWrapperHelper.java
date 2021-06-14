package com.github.yiuman.citrus.support.crud.query;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yiuman.citrus.support.cache.Cache;
import com.github.yiuman.citrus.support.crud.query.annotations.QueryParam;
import com.github.yiuman.citrus.support.utils.CacheUtils;
import com.github.yiuman.citrus.support.utils.ClassUtils;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import com.github.yiuman.citrus.support.utils.SpringUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 查询参数工具类
 *
 * @author yiuman
 * @date 2021/6/13
 */
public final class QueryWrapperHelper {

    /**
     * 参数类中需要处理查询字段的缓存，提高处理速度
     */
    private final static Cache<Class<?>, Set<QueryParamMeta>> CLASS_QUERY_META_CACHE = CacheUtils.newInMemoryCache("CLASS_QUERY_META_CACHE");

    private QueryWrapperHelper() {
    }

    /**
     * QueryParam注解转为QueryParamMeta
     *
     * @param metaClass 目标对象的类型
     * @param field     目标字段
     * @return 查询参数元实例
     */
    public static QueryParamMeta queryParamAnnotation2Meta(Class<?> metaClass, Field field) {
        QueryParam queryParam = AnnotatedElementUtils.getMergedAnnotation(field, QueryParam.class);
        if (Objects.nonNull(queryParam)) {
            return QueryParamMeta.builder()
                    .metaClass(metaClass)
                    .annotation(queryParam)
                    .field(field)
                    .type(queryParam.type())
                    .condition(queryParam.condition())
                    .mapping(queryParam.mapping())
                    .handlerClass(queryParam.handler())
                    .build();
        }
        return null;
    }

    public static void doInjectQueryWrapper(final QueryWrapper<?> wrapper, Object params) {
        Class<?> paramsClass = ClassUtils.getRealClass(params.getClass());
        final Set<QueryParamMeta> needHandlerFields = Optional.ofNullable(CLASS_QUERY_META_CACHE.find(paramsClass)).orElse(new HashSet<>());
        if (CollectionUtil.isEmpty(needHandlerFields)) {
            Arrays.stream(paramsClass.getDeclaredFields()).forEach(field -> {
                field.setAccessible(true);
                QueryParamMeta queryParamMeta = queryParamAnnotation2Meta(paramsClass, field);
                if (Objects.nonNull(queryParamMeta)) {
                    needHandlerFields.add(queryParamMeta);
                }

            });
            CLASS_QUERY_META_CACHE.save(paramsClass, needHandlerFields);
        }

        //遍历处理参数
        needHandlerFields.forEach(LambdaUtils.consumerWrapper(queryParamMeta -> {
            Class<? extends QueryParamHandler> handlerClass = queryParamMeta.getHandlerClass();
            if (handlerClass.isInterface() || Modifier.isAbstract(handlerClass.getModifiers())) {
                return;
            }
            QueryParamHandler queryParamHandler = SpringUtils.getBean(handlerClass, true);
            if (Objects.nonNull(queryParamHandler)) {
                queryParamHandler.handle(queryParamMeta, params, wrapper);
            }

        }));

    }
}
