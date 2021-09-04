package com.github.yiuman.citrus.support.utils;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yiuman.citrus.support.crud.mapper.CrudMapper;
import com.github.yiuman.citrus.support.crud.mapper.TreeMapper;
import com.github.yiuman.citrus.support.crud.rest.CrudRestful;
import com.github.yiuman.citrus.support.crud.rest.QueryRestful;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.model.Tree;
import com.github.yiuman.citrus.support.widget.Selections;
import com.github.yiuman.citrus.support.widget.Selects;
import com.github.yiuman.citrus.support.widget.Widget;
import javassist.CtClass;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.BindingException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * CRUD工具类
 *
 * @author yiuman
 * @date 2020/5/8
 */
@Slf4j
public final class CrudUtils {

    private CrudUtils() {
    }

    /**
     * 获取CrudService实例
     *
     * @param entityClass       实体class
     * @param keyClass          主键class
     * @param superServiceClass 父类的serviceClass
     * @param <E>               实体类型
     * @param <K>               主键类型
     * @param <S>               CrudService的子类类型
     * @return CrudService实例
     * @throws Exception 反射异常
     */
    public static <E, K extends Serializable, S extends CrudService<E, K>> S getCrudService(
            Class<E> entityClass,
            Class<K> keyClass,
            Class<S> superServiceClass) throws Exception {
        Class<? extends S> crudServiceClass = getCrudServiceClass(entityClass, keyClass, superServiceClass);
        return SpringUtils.getBean(crudServiceClass, true);
    }

    /**
     * 获取CrudService 增删改查逻辑类的Class（没有则创建）
     *
     * @param entityClass       实体class
     * @param keyClass          主键class
     * @param superServiceClass 父类的CrudService类型的Class
     * @param <E>               实体类型
     * @param <K>               主键类型
     * @param <S>               CrudService的子类类型
     * @return 增删改查逻辑类的Class （继承与CrudService子类）
     * @throws Exception 反射异常
     */
    @SuppressWarnings("unchecked")
    public static synchronized <E, K extends Serializable, S extends CrudService<E, K>> Class<S> getCrudServiceClass(
            Class<E> entityClass,
            Class<K> keyClass,
            Class<S> superServiceClass) throws Exception {
        String entityClassName = entityClass.getName();
        String formatName = String.format("%sCrudService$$javassist", entityClassName);
        Class<?> serviceClass;
        try {
            serviceClass = JavassistUtils.defaultPool().getClassLoader().loadClass(formatName);
        } catch (ClassNotFoundException e) {
            CtClass ctClass;
            try {
                ctClass = JavassistUtils.defaultPool().getCtClass(formatName);
                serviceClass = ctClass.getClass();
            } catch (NotFoundException notFoundCtClass) {
                ctClass = JavassistUtils.defaultPool().makeClass(formatName, JavassistUtils.getClass(superServiceClass));
                JavassistUtils.addTypeArgument(ctClass, superServiceClass, new Class[]{entityClass, keyClass}, null, null);
                serviceClass = ctClass.toClass();
            }
        }
        return (Class<S>) serviceClass;
    }


    /**
     * 动态构建CRUDMAPPER
     *
     * @param entityClass 实体类Class
     * @param <T>         实体类类型
     * @return CrudMapper接口的class
     * @throws Exception 反射异常，无法创建CLASS异常
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T> Class<? extends BaseMapper<T>> getMapperInterface(Class<T> entityClass, Class<?> mapperClass) throws Exception {
        String entityClassName = entityClass.getName();
        String mapperClassSimpleName = mapperClass.getSimpleName();
        String formatName = String.format("%s$$javassist", (entityClassName + mapperClassSimpleName));
        Class<?> mapperInterface;
        try {
            mapperInterface = JavassistUtils.defaultPool().getClassLoader().loadClass(formatName);
        } catch (ClassNotFoundException e) {
            CtClass ctClass;
            try {
                ctClass = JavassistUtils.defaultPool().getCtClass(formatName);
                mapperInterface = ctClass.getClass();
            } catch (NotFoundException notFoundCtClass) {
                ctClass = JavassistUtils.defaultPool().makeInterface(formatName, JavassistUtils.getClass(mapperClass));
                JavassistUtils.addTypeArgument(ctClass, null, null, mapperClass, new Class[]{entityClass});
                mapperInterface = ctClass.toClass();
            }

        }


        return (Class<? extends BaseMapper<T>>) mapperInterface;
    }


    public static <M extends CrudMapper<T>, T> M getCrudMapper(Class<T> entityClass) throws Exception {
        return getMapper(entityClass, CrudMapper.class);
    }

    public static <M extends TreeMapper<T>, T extends Tree<?>> M getTreeMapper(Class<T> entityClass) throws Exception {
        return getMapper(entityClass, TreeMapper.class);
    }

    /**
     * 根据实体类型，Mybatis的Mapper接口类型动态获取的实体映射接口 如：UserMapper extend BaseMapper<User></>
     * 第一次从SqlSessionTemplate取到直接返回，若取不到，会动态构建一个，通过字节码工具去生成字节码放到内存中
     *
     * @param entityClass 实体类型
     * @param <M>         生成的接口类型
     * @param <T>         实体类型
     * @return mapper接口的动态代理
     * @throws Exception 反射异常 Spring容器找不到的异常
     */
    @SuppressWarnings("unchecked")
    public static <M extends BaseMapper<T>, T> M getMapper(Class<T> entityClass, Class<?> baseMapperClass) throws Exception {
        Class<? extends BaseMapper<T>> mapperClass = getMapperInterface(entityClass, baseMapperClass);
        //多线程的使用 线程安全的SqlSessionTemplate
        SqlSessionTemplate sqlSessionTemplate = SpringUtils.getBean(SqlSessionTemplate.class);
        M mapper;
        try {
            mapper = (M) sqlSessionTemplate.getMapper(mapperClass);
        } catch (BindingException e) {
            sqlSessionTemplate.getConfiguration().addMapper(mapperClass);
            mapper = (M) sqlSessionTemplate.getMapper(mapperClass);
        }
        return mapper;

    }

    /**
     * 获取CRUD页面使用的小部件
     *
     * @param restful Crud
     * @return 小部件集合
     */
    public static <W extends Widget<?>> List<W> getCrudWidgets(QueryRestful<?, ?> restful) {
        final List<W> widgets = new LinkedList<>();
        ReflectionUtils.doWithMethods(restful.getClass(), method -> {
            try {
                widgets.add(getWidget(restful, method));
            } catch (Exception ex) {
                log.info("控件实例化报错", ex);
            }

        }, method -> method.getAnnotation(Selects.class) != null);
        return widgets;
    }


    public static <W extends Widget<?>> W getWidget(CrudRestful<?, ?> restful, String methodName) {
        Method method = ReflectionUtils.findMethod(restful.getClass(), methodName);
        try {
            return getWidget(restful, method);
        } catch (Throwable throwable) {
            log.warn("cannot find widget by crud ", throwable);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <W extends Widget<?>> W getWidget(Object object, Method method) throws InvocationTargetException, IllegalAccessException {
        if (method == null) {
            return null;
        }
        Selects selects = method.getAnnotation(Selects.class);
        if (selects == null) {
            return null;
        }
        String text = org.springframework.util.StringUtils.hasText(selects.text()) ? selects.text() : selects.key();

        method.setAccessible(true);
        Object invoked = method.invoke(object);
        final List<Selections.SelectItem> selectItems = new ArrayList<>();
        if (invoked instanceof Collection) {
            Collection<?> collects = (Collection<?>) invoked;
            //串行处理
            collects.forEach(LambdaUtils.consumerWrapper(item -> {
                String labelFieldName = org.springframework.util.StringUtils.hasText(selects.label())
                        ? selects.label()
                        : selects.key();
                Map<?, ?> beanJsonMap = ConvertUtils.bean2JsonMap(item);
                Object value = beanJsonMap.get(selects.key());
                String label;
                if (labelFieldName.equals(selects.key())) {
                    label = item.toString();
                } else {
                    Field labelField = ReflectionUtils.findField(item.getClass(), labelFieldName);
                    ReflectionUtils.makeAccessible(labelField);
                    label = ReflectionUtils.getField(labelField, item).toString();
                }

                selectItems.add(new Selections.SelectItem(selects.key(), label, value));
            }));
        }

        return (W) new Selections(text, selects.bind(), selectItems, selects.multiple());
    }

}
