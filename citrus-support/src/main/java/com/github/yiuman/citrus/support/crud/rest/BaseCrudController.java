package com.github.yiuman.citrus.support.crud.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yiuman.citrus.support.crud.CrudReadDataListener;
import com.github.yiuman.citrus.support.crud.QueryParam;
import com.github.yiuman.citrus.support.crud.QueryParamHandler;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.exception.ValidateException;
import com.github.yiuman.citrus.support.http.ResponseEntity;
import com.github.yiuman.citrus.support.model.SortBy;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import com.github.yiuman.citrus.support.utils.SpringUtils;
import com.github.yiuman.citrus.support.utils.ValidateUtils;
import com.github.yiuman.citrus.support.utils.WebUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 基础的RestfulCrud控制器
 *
 * @author yiuman
 * @date 2020/4/4
 */
@SuppressWarnings("unchecked")
public abstract class BaseCrudController<T, K extends Serializable> {

    /**
     * 查询参数类型
     */
    protected Class<?> paramClass;

    /**
     * 模型类型
     */
    protected Class<T> modelClass = currentModelClass();

    /**
     * 默认的排序的集合
     */
    protected List<SortBy> sortBIES = new ArrayList<>();

    private Class<T> currentModelClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(getClass(), 1);
    }

    protected void setParamClass(Class<?> paramClass) {
        if (paramClass.isInterface() || Modifier.isAbstract(paramClass.getModifiers())) {
            return;
        }
        this.paramClass = paramClass;
    }

    protected abstract CrudService<T, K> getService();

    /**
     * 添加排序项
     * 在构造中使用
     *
     * @param column 列
     * @param isDesc 是否倒序
     */
    protected void addSortBy(String column, boolean isDesc) {
        sortBIES.add(new SortBy(column, isDesc));
    }

    @GetMapping
    public ResponseEntity<Page<T>> page(HttpServletRequest request) throws Exception {
        //获取pageNo
        Page<T> page = new Page<>();
        WebUtils.requestDataBind(page, request);
        return ResponseEntity.ok(getService().page(page, queryWrapper(request)));
    }

    @PostMapping
    public ResponseEntity<K> save(@Validated T entity) throws Exception {
        return ResponseEntity.ok(getService().save(entity));
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Boolean> delete(@PathVariable @NotNull K key) throws Exception {
        return ResponseEntity.ok(getService().remove(getService().get(key)));
    }

    @PostMapping("/batch_delete")
    public ResponseEntity<Void> batchDelete(@NotNull List<K> keys) {
        getService().batchRemove(keys);
        return ResponseEntity.ok();
    }

    @GetMapping("/{key}")
    public ResponseEntity<T> get(@PathVariable K key) {
        return ResponseEntity.ok(getService().get(key));
    }

    /**
     * 导出
     */
    @GetMapping(value = "/export")
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        String fileName = Optional.ofNullable(WebUtils.getRequestParam("fileName"))
                .orElse(String.valueOf(System.currentTimeMillis()));
        WebUtils.exportExcel(response, modelClass, getService().list(queryWrapper(request)), fileName);
    }

    /**
     * 导入
     */
    @GetMapping(value = "/import")
    public void imp(MultipartFile file) throws Exception {
        //CrudReadDataListener不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
        WebUtils.importExcel(file, modelClass, new CrudReadDataListener<>(getService()));
    }

    /**
     * 构造查询wrapper
     *
     * @return QueryWrapper
     */
    protected QueryWrapper<T> queryWrapper(HttpServletRequest request) throws Exception {
        if (paramClass == null) {
            return null;
        }

        QueryWrapper<T> wrapper = Wrappers.query();
        Object params = WebUtils.requestDataBind(paramClass, request);
        if (params == null) {
            return wrapper;
        }
        //检验参数
        ValidateUtils.validateEntityAndThrows(params, result -> new ValidateException(result.getMessage()));
        //拼接查询条件
        handleQueryWrapper(wrapper, params);

        handleSortWrapper(wrapper, request);

        return wrapper;
    }

    protected void handleSortWrapper(QueryWrapper<T> wrapper, HttpServletRequest request) throws Exception {
        //构造默认的排序
        sortBIES.forEach(sortByItem -> wrapper.orderBy(true, !sortByItem.getSortDesc(), StringUtils.camelToUnderline(sortByItem.getSortBy())));
        //拼接排序条件
        SortBy sortBy = WebUtils.requestDataBind(SortBy.class, request);
        if (sortBy != null && org.springframework.util.StringUtils.hasText(sortBy.getSortBy())) {
            wrapper.orderBy(true, !sortBy.getSortDesc(), StringUtils.camelToUnderline(sortBy.getSortBy()));
        }

    }

    /**
     * 根据参数，处理查询wrapper
     *
     * @param wrapper QueryWrapper
     * @param params  参数对象
     */
    protected void handleQueryWrapper(final QueryWrapper<T> wrapper, Object params) {
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
                        if (ObjectUtils.isEmpty(value)) {
                            return;
                        }
                        Method conditionMethod = wrapper
                                .getClass()
                                .getMethod(annotation.type(), boolean.class, Object.class, Object.class);
                        conditionMethod.setAccessible(true);
                        conditionMethod.invoke(wrapper, annotation.condition(), StringUtils.camelToUnderline(field.getName()), field.get(params));
                    }
                }));
    }
}
