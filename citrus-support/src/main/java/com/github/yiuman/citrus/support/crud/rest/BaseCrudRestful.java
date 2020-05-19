package com.github.yiuman.citrus.support.crud.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.support.crud.CrudReadDataListener;
import com.github.yiuman.citrus.support.crud.QueryParam;
import com.github.yiuman.citrus.support.crud.QueryParamHandler;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.exception.ValidateException;
import com.github.yiuman.citrus.support.model.DialogView;
import com.github.yiuman.citrus.support.model.EditField;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.model.SortBy;
import com.github.yiuman.citrus.support.utils.*;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 基础的RESTFUL
 *
 * @author yiuman
 * @date 2020/5/11
 */
public abstract class BaseCrudRestful<T, K extends Serializable> implements CrudRestful<T, K> {

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

    @SuppressWarnings("unchecked")
    private Class<T> currentModelClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(getClass(), 0);
    }

    protected void setParamClass(Class<?> paramClass) {
        if (paramClass.isInterface() || Modifier.isAbstract(paramClass.getModifiers())) {
            return;
        }
        this.paramClass = paramClass;

    }

    protected abstract CrudService<T, K> getService();

    protected Page<T> createPage() throws Exception {
        Page<T> page = new Page<>();
        page.setItemKey(getService().getKeyName());
        //构造页面小部件
        CrudUtils.getCrudWidgets(this)
                .forEach(widget -> page.addWidget(widget, true));
        page.setDialogView(createDialogView());
        return page;
    }

    protected DialogView createDialogView() throws Exception {
        List<EditField> editFields = new ArrayList<>();
        ReflectionUtils.doWithFields(modelClass, field -> editFields.add(new EditField(field.getName(), field.getName())));
        return new DialogView(editFields);
    }


    @Override
    public Page<T> page(HttpServletRequest request) throws Exception {
        //获取pageNo
        Page<T> page = createPage();
        WebUtils.requestDataBind(page, request);
        QueryWrapper<T> queryWrapper = getQueryWrapper(request);
        handleSortWrapper(queryWrapper, request);
        return getService().page(page, queryWrapper);
    }

    @Override
    public K save(T entity) throws Exception {
        return getService().save(entity);
    }

    @Override
    public Boolean delete(K key) throws Exception {
        return getService().remove(getService().get(key));
    }

    @Override
    public T get(K key) {
        return getService().get(key);
    }

    @Override
    public void batchDelete(List<K> keys) throws Exception {
        getService().batchRemove(keys);
    }

    @Override
    public void imp(MultipartFile file) throws Exception {
        //CrudReadDataListener不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
        WebUtils.importExcel(file, modelClass, new CrudReadDataListener<>(getService()));
    }

    @Override
    public void exp(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        String fileName = Optional.ofNullable(WebUtils.getRequestParam("fileName"))
                .orElse(String.valueOf(System.currentTimeMillis()));
        WebUtils.exportExcel(response, modelClass, getService().list(getQueryWrapper(request)), fileName);
    }

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

    /**
     * 添加排序项
     * 在构造中使用
     *
     * @param column 列
     */
    protected void addSortBy(String column) {
        sortBIES.add(new SortBy(column, false));
    }


    /**
     * 构造查询wrapper
     *
     * @return QueryWrapper
     */
    protected QueryWrapper<T> getQueryWrapper(HttpServletRequest request) throws Exception {
        return getQueryWrapper(WebUtils.requestDataBind(paramClass, request));
    }

    protected QueryWrapper<T> getQueryWrapper(Object params) {
        if (params == null) {
            return null;
        }
        QueryWrapper<T> wrapper = Wrappers.query();
        //检验参数
        ValidateUtils.validateEntityAndThrows(params, result -> new ValidateException(result.getMessage()));
        //拼接查询条件
        handleQueryWrapper(wrapper, params);
        if(!org.springframework.util.StringUtils.hasText(wrapper.getTargetSql())){
            return null;
        }
        return wrapper;
    }

    /**
     * 处理排序
     *
     * @param wrapper 查询wrapper
     * @param request 当前的请求
     * @throws Exception 绑定数据时发生的异常
     */
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
    protected void handleQueryWrapper(final QueryWrapper<T> wrapper, Object params)  {
        Arrays.stream(paramClass.getDeclaredFields())
                .filter(field -> field.getAnnotation(QueryParam.class) != null)
                .forEach(LambdaUtils.consumerWrapper(field -> {
                            QueryParam annotation = field.getAnnotation(QueryParam.class);
                            Class<? extends QueryParamHandler> handlerClass = annotation.handler();
                            if (!handlerClass.isInterface()) {
                                QueryParamHandler handler = SpringUtils.getBean(handlerClass);
                                handler.handle(annotation, params, field, wrapper);
                            }
                        })
                );
    }

}
