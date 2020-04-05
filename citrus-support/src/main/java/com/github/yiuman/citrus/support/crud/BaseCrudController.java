package com.github.yiuman.citrus.support.crud;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yiuman.citrus.support.http.ResponseEntity;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import com.github.yiuman.citrus.support.utils.SpringUtils;
import com.github.yiuman.citrus.support.utils.ValidateUtils;
import com.github.yiuman.citrus.support.utils.WebUtils;
import com.google.common.base.VerifyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * 基础的RestfulCrud控制器
 *
 * @author yiuman
 * @date 2020/4/4
 */
public abstract class BaseCrudController<S extends CrudService<T, K>, T, K> {

    private static final String PAGE_NO_PARAMETER = "pageNo";

    @Autowired
    @NotNull
    protected S service;

    private Class<?> paramClass;

    protected void setParamClass(Class<?> paramClass) {
        if (paramClass.isInterface() || Modifier.isAbstract(paramClass.getModifiers())) {
            return;
        }
        this.paramClass = paramClass;
    }

    @GetMapping
    public ResponseEntity<Page<T>> list(HttpServletRequest request) throws Exception {
        //获取pageNo
        Page<T> page = new Page<>();
        WebUtils.requestDataBind(page, request);
        return ResponseEntity.ok(service.selectPage(page, queryWrapper(request)));
    }

    @PostMapping
    public ResponseEntity<K> save(@Validated T entity) throws Exception {
        return ResponseEntity.ok(service.saveEntity(entity));
    }

    @DeleteMapping("{key}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull K key) throws Exception {
        service.delete(key);
        return ResponseEntity.ok();
    }

    @GetMapping("{key}")
    public ResponseEntity<T> get(@PathVariable K key) throws Exception {
        return ResponseEntity.ok(service.get(key));
    }

    /**
     * 构造查询wrapper
     *
     * @return QueryWrapper
     */
    protected QueryWrapper<T> queryWrapper(HttpServletRequest request) {
        if (paramClass == null) {
            return null;
        }

        QueryWrapper<T> wrapper = new QueryWrapper<>();
        Object params = WebUtils.requestDataBind(paramClass, request);
        //检验参数
        ValidateUtils.validateEntityAndThrows(params, result -> new VerifyException(result.getMessage()));
        handleWrapper(wrapper, params);
        return wrapper;
    }

    /**
     * 根据参数，处理查询wrapper
     *
     * @param wrapper QueryWrapper
     * @param params  参数对象
     */
    protected void handleWrapper(final QueryWrapper<T> wrapper, Object params) {
        Arrays.stream(paramClass.getDeclaredFields())
                .filter(field -> field.getAnnotation(QueryParam.class) != null)
                .forEach(LambdaUtils.consumerWrapper(field -> {
                    QueryParam annotation = field.getAnnotation(QueryParam.class);
                    Class<? extends QueryParamHandler> handlerClass = annotation.handler();
                    if (!handlerClass.isInterface()) {
                        QueryParamHandler handler = SpringUtils.getBean(handlerClass);
                        handler.handle(annotation, params, field, wrapper);
                    } else {
                        field.setAccessible(true);
                        Object value = field.get(params);
                        if(ObjectUtils.isEmpty(value)){
                            return;
                        }
                        Method conditionMethod = wrapper
                                .getClass()
                                .getMethod(annotation.type(), boolean.class, Object.class, Object.class);
                        conditionMethod.setAccessible(true);
                        conditionMethod.invoke(wrapper, annotation.condition(), field.getName(), field.get(params));
                    }
                }));
    }
}
