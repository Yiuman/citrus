package com.github.yiuman.citrus.support.utils;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yiuman.citrus.support.crud.mapper.CrudMapper;
import com.github.yiuman.citrus.support.crud.mapper.TreeMapper;
import com.github.yiuman.citrus.support.crud.rest.CrudRestful;
import com.github.yiuman.citrus.support.widget.Selections;
import com.github.yiuman.citrus.support.widget.Selects;
import com.github.yiuman.citrus.support.widget.Widget;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.SignatureAttribute;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.BindingException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
     * 动态构建CRUDMAPPER
     *
     * @param entityClass 实体类Class
     * @param <T>         实体类类型
     * @return CrudMapper接口的class
     * @throws Exception 反射异常，无法创建CLASS异常
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<? extends BaseMapper<T>> getMapperInterface(Class<T> entityClass, Class<?> mapperClass) throws Exception {
        String entityClassName = entityClass.getName();
        String formatName = String.format("%sMapper$$javassist", entityClassName);
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
                //实现类型type
                SignatureAttribute.ClassType classType = new SignatureAttribute.ClassType(entityClassName);

                //相应的泛型参数定义
                SignatureAttribute.TypeArgument typeArgument = new SignatureAttribute.TypeArgument(classType);

                //完整的接口泛型描述
                SignatureAttribute.ClassType interfaceClassType = new SignatureAttribute
                        .ClassType(mapperClass.getName(), new SignatureAttribute.TypeArgument[]{typeArgument});
                //实现类的泛型描述 实现类的泛型组成 自身泛型,父类泛型,接口泛型[], 因此这里均没有,则实际传入null
                ctClass.setGenericSignature(new SignatureAttribute
                        .ClassSignature(null, null, new SignatureAttribute.ClassType[]{interfaceClassType})
                        .encode());
                mapperInterface = ctClass.toClass();
            }

        }


        return (Class<? extends BaseMapper<T>>) mapperInterface;
    }


    @SuppressWarnings("unchecked")
    public static <M extends BaseMapper<T>, T> M getCrudMapper(Class<T> entityClass) throws Exception {
        return (M) getMapper(entityClass, CrudMapper.class);
    }

    @SuppressWarnings("unchecked")
    public static <M extends BaseMapper<T>, T> M getTreeMapper(Class<T> entityClass) throws Exception {
        return (M) getMapper(entityClass, TreeMapper.class);
    }

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
    public static <W extends Widget<?>> List<W> getCrudWidgets(CrudRestful<?, ?> restful) {
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


    public static <W extends Widget<?>> W getWidget(CrudRestful<?, ?> restful, String methodName) throws InvocationTargetException, IllegalAccessException {
        Method method = ReflectionUtils.findMethod(restful.getClass(), methodName);
        return getWidget(restful, method);
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
            collects.parallelStream().forEach(LambdaUtils.consumerWrapper(item -> {
                String labelFieldName = org.springframework.util.StringUtils.hasText(selects.label())
                        ? selects.label()
                        : selects.key();
                Field valueField = ReflectionUtils.findField(item.getClass(), selects.key());
                if (valueField == null) {
                    return;
                }
                valueField.setAccessible(true);
                Object value = valueField.get(item);
                String label;
                if (labelFieldName.equals(selects.key())) {
                    label = value == null ? "" : value.toString();
                } else {
                    Field labelField = ReflectionUtils.findField(item.getClass(), labelFieldName);
                    ReflectionUtils.makeAccessible(labelField);
                    label = ReflectionUtils.getField(labelField, item).toString();
                }

                selectItems.add(new Selections.SelectItem(selects.key(), label, value.toString()));
            }));
        }

        return (W) new Selections(text, selects.bind(), selectItems, selects.multiple());
    }
}
