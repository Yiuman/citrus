package com.github.yiuman.citrus.support.crud.rest;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yiuman.citrus.support.crud.query.Fn;
import com.github.yiuman.citrus.support.crud.query.Query;
import com.github.yiuman.citrus.support.crud.query.QueryHelper;
import com.github.yiuman.citrus.support.crud.query.builder.QueryBuilders;
import com.github.yiuman.citrus.support.crud.query.builder.SimpleQueryBuilder;
import com.github.yiuman.citrus.support.crud.view.impl.SimpleTableView;
import com.github.yiuman.citrus.support.inject.InjectAnnotationParserHolder;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.model.SortBy;
import com.github.yiuman.citrus.support.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @param <T> 实体类型
 * @param <K> 主键类型
 * @author yiuman
 * @date 2020/10/1
 */
@Slf4j
public abstract class BaseQueryRestful<T, K extends Serializable> extends BaseRestful<T, K> implements QueryRestful<T, K> {

    /**
     * 查询参数类型
     */
    protected Class<?> paramClass;

    /**
     * 默认的排序的集合
     */
    protected List<SortBy> sortByList = new ArrayList<>();

    public BaseQueryRestful() {
    }

    protected void setParamClass(Class<?> paramClass) {
        if (paramClass.isInterface() || Modifier.isAbstract(paramClass.getModifiers())) {
            return;
        }
        this.paramClass = paramClass;
    }

    /**
     * 创建列表分页页面
     *
     * @return 分页页面对象
     */
    protected Object createView(List<T> records) {
        SimpleTableView<T> view = new SimpleTableView<>();
        //构造页面小部件
        CrudUtils.getCrudWidgets(this).forEach(widget -> view.addWidget(widget, true));
        //构造默认表头
        ReflectionUtils.doWithFields(modelClass, field -> view.addHeader(field.getName(), field.getName()));
        return view;
    }

    @Override
    public Page<T> page(HttpServletRequest request) throws Exception {
        Query query = Optional.ofNullable(getQueryCondition(request)).orElse(Query.create());
        handleSortQuery(query, request);
        //获取pageNo
        Page<T> page = new Page<>();
        //绑定页面参数
        WebUtils.requestDataBind(page, request);

        //这里需要调用了page方法查询后再进行设置ItemKey,原因是Service中的mapper为动态注入，调用查询才会初始化mapper构造表信息
        Page<T> realPage = selectPage(page, query);
        if (StringUtils.isBlank(realPage.getItemKey())) {
            realPage.setItemKey(getService().getKeyProperty());
        }

        realPage.setView(createView(realPage.getRecords()));

        return realPage;
    }

    /**
     * 根据分页条件，查询条件进行分页查询
     *
     * @param page  分页对象
     * @param query 查询条件
     * @return 查询后的分页数据
     */
    protected Page<T> selectPage(Page<T> page, Query query) {
        return getService().page(page, query);
    }

    @Override
    public T get(K key) {
        return getService().get(key);
    }

    @Override
    public void exp(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileName = WebUtils.getRequestParam("filename");
        if (ObjectUtil.isEmpty(fileName)) {
            fileName = String.valueOf(System.currentTimeMillis());
        }

        Query query = Optional.ofNullable(getQueryCondition(request)).orElse(Query.create());
        handleSortQuery(query, request);
        Page<T> page = new Page<>();

        page.setSize(-1);
        page = selectPage(page, query);
        if (StringUtils.isBlank(page.getItemKey())) {
            page.setItemKey(getService().getKeyProperty());
        }

        page.setView(createView(page.getRecords()));
        WebUtils.exportExcel(response, page, fileName);
    }

    /**
     * 添加排序项
     * 在构造中使用
     *
     * @param column 列
     * @param isDesc 是否倒序
     */
    protected void addSortBy(String column, boolean isDesc) {
        sortByList.add(new SortBy(column, isDesc));
    }

    /**
     * 添加排序项
     * 在构造中使用
     *
     * @param column 列
     */
    @SuppressWarnings({"unused", "RedundantSuppression"})
    protected void addSortBy(String column) {
        sortByList.add(new SortBy(column, false));
    }

    protected void addSortBy(Fn<T, ?> fn, boolean isDesc) {
        sortByList.add(new SortBy(LambdaUtils.getPropertyName(fn), isDesc));
    }

    protected void addSortBy(Fn<T, ?> fn) {
        sortByList.add(new SortBy(LambdaUtils.getPropertyName(fn), false));
    }

    /**
     * 构造查询
     *
     * @return Query
     */
    @Override
    public Query getQueryCondition(HttpServletRequest request) throws Exception {
        return getQueryCondition(getQueryParams(request));
    }

    @Override
    public Object getQueryParams(HttpServletRequest request) throws Exception {
        //将请求转化成参数
        Object params = WebUtils.requestDataBind(paramClass, request, true);
        //注解注入
        if (Objects.nonNull(paramClass)) {
            if (Objects.isNull(params)) {
                params = BeanUtils.instantiateClass(paramClass);
            }
            SpringUtils.getBean(InjectAnnotationParserHolder.class, true).inject(params);
        }

        return params;
    }

    protected Query getQueryCondition(Object params) {
        if (Objects.isNull(params)) {
            return null;
        }

        //检验参数
        ValidateUtils.validateEntityAndThrows(params, result -> new ValidateException(result.getMessage()));

        Query query = Query.create();
        //拼接查询条件
        QueryHelper.doInjectQuery(query, params);
        return query;
    }

    /**
     * 处理排序
     *
     * @param query   查询构造
     * @param request 当前的请求
     * @throws Exception 绑定数据时发生的异常
     */
    protected void handleSortQuery(Query query, HttpServletRequest request) throws Exception {
        SimpleQueryBuilder wrapper = QueryBuilders.wrapper(query);
        final Consumer<SortBy> sortItemHandler = wrapper::orderBy;
        //拼接排序条件
        SortBy sortBy = WebUtils.requestDataBind(SortBy.class, request);
        if (Objects.nonNull(sortBy) && org.springframework.util.StringUtils.hasText(sortBy.getSortBy())) {
            sortItemHandler.accept(sortBy);
            sortByList.stream().filter(sortByItem -> !sortByItem.getSortBy().equals(sortBy.getSortBy()))
                    .forEach(sortItemHandler);

        } else {
            //构造默认的排序
            sortByList.forEach(sortItemHandler);
        }

    }

}
