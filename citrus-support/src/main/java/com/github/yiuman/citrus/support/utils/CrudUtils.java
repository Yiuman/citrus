package com.github.yiuman.citrus.support.utils;

import com.github.yiuman.citrus.support.crud.rest.CrudRestful;
import com.github.yiuman.citrus.support.widget.Selections;
import com.github.yiuman.citrus.support.widget.Selects;
import com.github.yiuman.citrus.support.widget.Widget;
import lombok.extern.slf4j.Slf4j;
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
