package com.github.yiuman.citrus.support.crud;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.TypeUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.github.yiuman.citrus.support.cache.InMemoryCache;
import com.github.yiuman.citrus.support.crud.mapper.CrudMapper;
import com.github.yiuman.citrus.support.crud.mapper.TreeMapper;
import com.github.yiuman.citrus.support.crud.service.BaseService;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.model.Tree;
import com.github.yiuman.citrus.support.utils.CacheUtils;
import com.github.yiuman.citrus.support.utils.CrudUtils;
import com.github.yiuman.citrus.support.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.binding.MapperRegistry;
import org.mybatis.spring.SqlSessionTemplate;

import java.io.Serializable;
import java.util.Objects;

/**
 * Crud辅助类，用于动态创建Service、Mapper
 *
 * @author yiuman
 * @date 2021/7/8
 */
@Slf4j
public final class CrudHelper {

    /**
     * 字节码动态CrudService缓存
     */
    private final static InMemoryCache<Class<?>, CrudService<?, ? extends Serializable>> SERVICE_CACHE
            = CacheUtils.newInMemoryCache("SERVICE_CACHE");

    /**
     * 实体类型与mapper接口的映射信息
     */
    private final static InMemoryCache<Class<?>, Class<? extends BaseMapper<?>>> MAPPER_CACHE
            = CacheUtils.newInMemoryCache("MAPPER_CACHE");

    private CrudHelper() {
    }

    /**
     * 动态获取CrudService
     *
     * @param entityClass 实体Class
     * @param keyClass    实体的主键类型Class
     * @param <E>         实体泛型
     * @param <K>         实体主键泛型
     * @param <S>         返回的Service泛型
     * @return 由Spring管理的动态创建的CrudService
     */
    @SuppressWarnings("unchecked")
    public static <E, K extends Serializable, S extends CrudService<E, K>> S getCrudService(
            Class<E> entityClass,
            Class<K> keyClass) {
        CrudService<?, ? extends Serializable> crudService = SERVICE_CACHE.find(entityClass);
        if (Objects.nonNull(crudService)) {
            return (S) crudService;
        }
        try {
            crudService = CrudUtils.getCrudService(entityClass, keyClass, BaseService.class);
        } catch (Throwable e) {
            log.error("Cannot auto create baseService for entity {}", TypeUtil.getTypeArgument(entityClass, 0), e);
            throw new RuntimeException(e);
        }
        SERVICE_CACHE.save(entityClass, crudService);
        return (S) crudService;
    }


    /**
     * 根据CrudService的实现类获取基础的CrudService,即BaseCrudService
     *
     * @param genericsClass CrudService的实现类型
     * @param <E>           实体泛型
     * @param <K>           主键泛型
     * @param <S>           CrudService实现
     * @return BaseCrudService
     */
    @SuppressWarnings("unchecked")
    public static <E, K extends Serializable, S extends CrudService<E, K>> S getCrudService(Class<?> genericsClass) {
        return getCrudService(
                (Class<E>) TypeUtil.getTypeArgument(genericsClass, 0),
                (Class<K>) TypeUtil.getTypeArgument(genericsClass, 1)
        );
    }

    /**
     * 根据实体以及基础Mapper接口获取Mapper
     *
     * @param entityClass     实体Class
     * @param baseMapperClass 基础Mapper基础的Class
     * @param <M>             Mapper泛型
     * @param <T>             实体泛型
     * @return 从Mybatis取出的Mapper代理对象（可能为动态字节码Mapper）
     */
    @SuppressWarnings("unchecked")
    public static <M extends BaseMapper<T>, T> M getMapper(Class<T> entityClass, Class<?> baseMapperClass) {
        try {
            //多线程的使用 线程安全的SqlSessionTemplate
            SqlSessionTemplate sqlSessionTemplate = SpringUtils.getBean(SqlSessionTemplate.class);
            //先看下Mapper与实体映射的缓存是否为空,为空则初始化已注册的实体缓存信息
            if (CollectionUtil.isEmpty(MAPPER_CACHE.keys())) {
                //找到Mapper注册器
                MapperRegistry mapperRegistry = sqlSessionTemplate.getConfiguration().getMapperRegistry();
                mapperRegistry.getMappers().stream().filter(mapperInterface -> {
                    Class<?>[] interfaces = mapperInterface.getInterfaces();
                    //找到是CrudMapper的实现
                    return ArrayUtil.isNotEmpty(interfaces) && interfaces[0].isAssignableFrom(CrudMapper.class);
                }).forEach(baseMapperInterface ->
                        MAPPER_CACHE.save(
                                (Class<?>) TypeUtil.getTypeArgument(baseMapperInterface, 0),
                                (Class<? extends BaseMapper<?>>) baseMapperInterface
                        )
                );
            }

            Class<? extends BaseMapper<?>> mapperClass = MAPPER_CACHE.find(entityClass);

            if (Objects.isNull(mapperClass)) {
                mapperClass = CrudUtils.getMapperInterface(entityClass, baseMapperClass);
                MAPPER_CACHE.save(entityClass, mapperClass);
            }

            M mapper;
            synchronized (mapperClass) {
                try {
                    mapper = (M) sqlSessionTemplate.getMapper(mapperClass);
                } catch (BindingException e) {
                    sqlSessionTemplate.getConfiguration().addMapper(mapperClass);
                    mapper = (M) sqlSessionTemplate.getMapper(mapperClass);
                }
            }
            return mapper;
        } catch (Throwable throwable) {
            log.error("Cannot auto get mapper for entity {} and mapperInterface {}", entityClass, baseMapperClass, throwable);
            throw new RuntimeException(throwable);
        }

    }

    public static <M extends CrudMapper<T>, T> M getCrudMapper(Class<T> entityClass) {
        return getMapper(entityClass, CrudMapper.class);
    }

    public static <M extends TreeMapper<T>, T extends Tree<?>> M getTreeMapper(Class<T> entityClass) {
        return getMapper(entityClass, TreeMapper.class);
    }

    public static TableInfo getTableInfo(Class<?> entityClass) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
        if (Objects.isNull(tableInfo) && Objects.isNull(MAPPER_CACHE.find(entityClass))) {
            getCrudMapper(entityClass);
        }
        return TableInfoHelper.getTableInfo(entityClass);
    }

}
