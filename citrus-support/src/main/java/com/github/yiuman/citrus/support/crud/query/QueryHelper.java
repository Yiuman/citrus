package com.github.yiuman.citrus.support.crud.query;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.github.yiuman.citrus.support.crud.query.annotations.QueryParam;
import com.github.yiuman.citrus.support.model.SortBy;
import com.github.yiuman.citrus.support.utils.ClassUtils;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import com.github.yiuman.citrus.support.utils.SpringUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author yiuman
 * @date 2021/8/15
 */
public final class QueryHelper {

    /**
     * 参数类中需要处理查询字段的缓存，提高处理速度
     */
    private static final Map<Class<?>, Set<QueryParamMeta>> CLASS_QUERY_META_CACHE = new ConcurrentHashMap<>(256);

    private QueryHelper() {
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
                    .operator(queryParam.operator())
                    .condition(queryParam.condition())
                    .mapping(queryParam.mapping())
                    .handlerClass(queryParam.handler())
                    .build();
        }
        return null;
    }

    public static void doInjectQuery(final Query query, Object params) {
        Class<?> paramsClass = ClassUtils.getRealClass(params.getClass());
        final Set<QueryParamMeta> needHandlerFields = Optional.ofNullable(CLASS_QUERY_META_CACHE.get(paramsClass)).orElse(new HashSet<>());
        if (CollectionUtil.isEmpty(needHandlerFields)) {
            Arrays.stream(paramsClass.getDeclaredFields()).forEach(field -> {
                field.setAccessible(true);
                QueryParamMeta queryParamMeta = queryParamAnnotation2Meta(paramsClass, field);
                if (Objects.nonNull(queryParamMeta)) {
                    needHandlerFields.add(queryParamMeta);
                }

            });
            CLASS_QUERY_META_CACHE.put(paramsClass, needHandlerFields);
        }

        //遍历处理参数
        needHandlerFields.forEach(LambdaUtils.consumerWrapper(queryParamMeta -> {
            Class<? extends QueryParamHandler> handlerClass = queryParamMeta.getHandlerClass();
            if (handlerClass.isInterface() || Modifier.isAbstract(handlerClass.getModifiers())) {
                return;
            }
            QueryParamHandler queryParamHandler = SpringUtils.getBean(handlerClass, true);
            if (Objects.nonNull(queryParamHandler)) {
                queryParamHandler.handle(queryParamMeta, params, query);
            }

        }));

    }

    public static QueryWrapper<?> getQueryWrapper(Query query) {
        return getQueryWrapper(query, query.getEntityClass());
    }

    public static <E> QueryWrapper<E> getQueryWrapper(Query query, Class<E> entityClass) {
        QueryWrapper<E> queryWrapper = Wrappers.query();
        queryWrapper.setEntityClass(entityClass);
        //拼接查询条件
        Map<String, ColumnCache> columnMap = com.baomidou.mybatisplus.core.toolkit.LambdaUtils.getColumnMap(entityClass);
        if (CollUtil.isNotEmpty(query.getConditions())) {
            query.getConditions().forEach(LambdaUtils.consumerWrapper(conditionInfo -> {
                Class<?> lastParameterType = Operations.IN_SQL.getType().equals(conditionInfo.getOperator())
                        || Operations.NOT_IN.getType().equals(conditionInfo.getOperator())
                        ? String.class
                        : getParameterClass(conditionInfo.getType());
                Method conditionMethod = queryWrapper
                        .getClass()
                        .getMethod(conditionInfo.getOperator(), boolean.class, Object.class, lastParameterType);
                conditionMethod.setAccessible(true);
                String fieldName = ObjectUtil.isNotEmpty(conditionInfo.getMapping())
                        ? conditionInfo.getMapping()
                        : conditionInfo.getParameter();
                ColumnCache columnCache = columnMap.get(com.baomidou.mybatisplus.core.toolkit.LambdaUtils.formatKey(fieldName));
                conditionMethod.invoke(
                        queryWrapper,
                        true,
                        Objects.nonNull(columnCache) ? columnCache.getColumn() : fieldName,
                        conditionInfo.getValue()
                );
            }));
        }

        if (CollUtil.isNotEmpty(query.getSorts())) {
            final Consumer<SortBy> sortItemHandler = (sortBy) -> {
                ColumnCache columnCache = columnMap.get(com.baomidou.mybatisplus.core.toolkit.LambdaUtils.formatKey(sortBy.getSortBy()));
                queryWrapper.orderBy(true, !sortBy.getSortDesc(), Objects.nonNull(columnCache) ? columnCache.getColumn() : sortBy.getSortBy());
            };
            query.getSorts().forEach(sortItemHandler);
        }

        return queryWrapper;
    }

    private static Class<?> getParameterClass(Class<?> clazz) {
        if (clazz.isArray()) {
            return Object[].class;
        }

        if (Collection.class.isAssignableFrom(clazz)) {
            return Collection.class;
        }

        return Object.class;
    }
}
