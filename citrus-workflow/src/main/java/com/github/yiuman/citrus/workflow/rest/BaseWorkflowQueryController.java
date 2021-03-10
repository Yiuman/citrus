package com.github.yiuman.citrus.workflow.rest;

import com.github.yiuman.citrus.support.crud.rest.BaseQueryController;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.utils.WebUtils;
import com.github.yiuman.citrus.workflow.exception.WorkflowException;
import com.github.yiuman.citrus.workflow.service.WorkflowService;
import com.github.yiuman.citrus.workflow.service.impl.WorkflowServiceImpl;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.query.Query;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 流程基础的查询控制器
 *
 * @author yiuman
 * @date 2021/3/8
 */
public abstract class BaseWorkflowQueryController<E, K extends Serializable>
        extends BaseQueryController<E, K> {

    private WorkflowService workflowService;

    /**
     * 对应实体匹配的查询器
     */
    private final Map<Class<?>, Supplier<? extends Query<?, ?>>> QUERY_MAPPING =
            new HashMap<>();

    public BaseWorkflowQueryController() {

        QUERY_MAPPING.put(ProcessDefinition.class, () -> getProcessEngine()
                .getRepositoryService()
                .createProcessDefinitionQuery());

        QUERY_MAPPING.put(Task.class, () -> getProcessEngine()
                .getTaskService()
                .createTaskQuery());

        QUERY_MAPPING.put(HistoricActivityInstance.class, () -> getProcessEngine()
                .getHistoryService()
                .createHistoricActivityInstanceQuery());
    }

    /**
     * 获取流程服务类
     *
     * @return 默认使用系统默认的流程服务类实现
     */
    protected WorkflowService getProcessService() {
        return workflowService = Optional.ofNullable(workflowService)
                .orElse(new WorkflowServiceImpl());
    }

    protected void setProcessService(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    protected ProcessEngine getProcessEngine() {
        return getProcessService().getProcessEngine();
    }

    @Override
    protected CrudService<E, K> getService() {
        return null;
    }

    @Override
    public Page<E> page(HttpServletRequest request) throws Exception {
        Page<E> page = new Page<>();
        WebUtils.requestDataBind(page, request);
        page.setTotal(getQuery().count());
        page.setRecords(
                getPageable(
                        getQueryParams(request),
                        (int) page.getCurrent(),
                        (int) page.getSize()
                )
        );
        page.setView(createView());
        page.setItemKey("id");
        return page;
    }

    protected <Q extends Query<Q, E>> List<E> getPageable(Object params, int current, int pageSize) {
        Q query = getQuery();
        if (Objects.nonNull(paramClass) && Objects.nonNull(params)) {
            ReflectionUtils.doWithFields(paramClass, (field) -> {
                field.setAccessible(true);
                Object methodAttr = field.get(params);
                if (!ObjectUtils.isEmpty(methodAttr)) {
                    try {
                        Method method = query.getClass().getMethod(field.getName(), field.getType());
                        method.invoke(query, methodAttr);
                    } catch (Exception ignore) {
                    }
                }

            });
        }

        Function<E, ? extends E> transformFunc = getTransformFunc();
        List<E> pageList = query.listPage(current - 1, pageSize);
        return Objects.nonNull(transformFunc) ? pageList.stream().map(transformFunc).collect(Collectors.toList()) : pageList;
    }

    /**
     * 获取流程引擎对应实体的查询对象
     *
     * @param <Q> 查询对象
     * @return 匹配的查询对象
     */
    @SuppressWarnings("unchecked")
    protected <Q extends Query<Q, E>> Q getQuery() {
        Supplier<? extends Query<?, ?>> supplier = Optional.ofNullable(QUERY_MAPPING.get(modelClass))
                .orElseThrow(() -> new WorkflowException(String.format("cannot found query's supplier for %s,please overwrite method `getQuery`", modelClass)));
        return (Q) supplier.get();
    }

    /**
     * 转化，用于列表查询后，自定的转化
     *
     * @return 转化的函数
     */
    protected Function<E, ? extends E> getTransformFunc() {
        return null;
    }

}
