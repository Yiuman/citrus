package com.github.yiuman.citrus.support.crud.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.support.crud.CrudReadDataListener;
import com.github.yiuman.citrus.support.crud.query.QueryParam;
import com.github.yiuman.citrus.support.crud.query.QueryParamHandler;
import com.github.yiuman.citrus.support.crud.service.BaseService;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.exception.ValidateException;
import com.github.yiuman.citrus.support.inject.InjectAnnotationParserHolder;
import com.github.yiuman.citrus.support.model.DialogView;
import com.github.yiuman.citrus.support.model.EditField;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.model.SortBy;
import com.github.yiuman.citrus.support.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 基础的RESTFUL
 *
 * @author yiuman
 * @date 2020/5/11
 */
@Slf4j
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
    protected List<SortBy> sortByList = new ArrayList<>();

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

    /**
     * 获取CRUD逻辑层服务类
     *
     * @return 实现了 CrudService的逻辑层
     */
    @SuppressWarnings("unchecked")
    protected CrudService<T, K> getService() {
        try {
            return CrudUtils.getCrudService(
                    modelClass,
                    (Class<K>) ReflectionKit.getSuperClassGenericType(getClass(), 1),
                    BaseService.class);
        } catch (Exception e) {
            log.info("获取CrudService报错", e);
            return null;
        }

    }

    /**
     * 创建列表分页页面
     *
     * @return 分页页面对象
     * @throws Exception 反射、数据库操作等异常
     */
    protected Page<T> createPage() throws Exception {
        Page<T> page = new Page<>();
        //构造页面小部件
        CrudUtils.getCrudWidgets(this)
                .forEach(widget -> page.addWidget(widget, true));
        page.setDialogView(createDialogView());
        return page;
    }

    /**
     * 创建数据编辑的对话框
     *
     * @return 对话框视图
     * @throws Exception 反射等操作异常
     */
    protected DialogView createDialogView() throws Exception {
        List<EditField> editFields = new ArrayList<>();
        ReflectionUtils.doWithFields(modelClass, field -> editFields.add(new EditField(field.getName(), field.getName())));
        return new DialogView(editFields);
    }

    @Override
    public Page<T> page(HttpServletRequest request) throws Exception {
        //获取pageNo
        Page<T> page = createPage();
        //添加默表头
        if (CollectionUtils.isEmpty(page.getHeaders())) {
            ReflectionUtils.doWithFields(modelClass, field -> page.addHeader(field.getName(), field.getName()));
        }

        //绑定页面参数
        WebUtils.requestDataBind(page, request);

        QueryWrapper<T> queryWrapper = Optional.ofNullable(getQueryWrapper(request)).orElse(Wrappers.query());
        handleSortWrapper(queryWrapper, request);

        //这里需要调用了page方法查询后再进行设置ItemKey,原因是Service中的mapper为动态注入，调用查询才会初始化mapper构造表信息
        Page<T> realPage = getService().page(page, queryWrapper);
        realPage.setItemKey(getService().getKeyProperty());
        return realPage;
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
        String fileName = WebUtils.getRequestParam("fileName");
        if (StringUtils.isBlank(fileName)) {
            fileName = String.valueOf(System.currentTimeMillis());
        }
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
        sortByList.add(new SortBy(column, isDesc));
    }

    /**
     * 添加排序项
     * 在构造中使用
     *
     * @param column 列
     */
    protected void addSortBy(String column) {
        sortByList.add(new SortBy(column, false));
    }


    /**
     * 构造查询wrapper
     *
     * @return QueryWrapper
     */
    @Override
    public QueryWrapper<T> getQueryWrapper(HttpServletRequest request) throws Exception {
        //将请求转化成参数
        Object params = WebUtils.requestDataBind(paramClass, request, true);
        //注解注入
        if (paramClass != null) {
            if (Objects.isNull(params)) {
                params = BeanUtils.instantiateClass(paramClass);
            }
            SpringUtils.getBean(InjectAnnotationParserHolder.class, true).inject(params);
        }

        return getQueryWrapper(params);
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
        if (!org.springframework.util.StringUtils.hasText(wrapper.getTargetSql())) {
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
        sortByList.forEach(sortByItem -> wrapper.orderBy(true, !sortByItem.getSortDesc(), StringUtils.camelToUnderline(sortByItem.getSortBy())));
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
                                QueryParamHandler handler = SpringUtils.getBean(handlerClass, true);
                                handler.handle(annotation, params, field, wrapper);
                            }
                        })
                );
    }

}
