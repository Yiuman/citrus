package com.github.yiuman.citrus.workflow.rest;

import com.github.yiuman.citrus.support.crud.rest.BaseQueryController;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.crud.service.KeyBasedService;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.utils.SpringUtils;
import com.github.yiuman.citrus.support.utils.WebUtils;
import com.github.yiuman.citrus.workflow.exception.WorkflowException;
import com.github.yiuman.citrus.workflow.service.WorkflowService;
import com.github.yiuman.citrus.workflow.service.impl.WorkflowServiceImpl;
import lombok.SneakyThrows;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
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
        extends BaseQueryController<E, K> implements KeyBasedService<E, K> {

    private WorkflowService workflowService;

    /**
     * 对应实体匹配的查询器
     */
    private final Map<Class<?>, Supplier<? extends Query<?, ?>>> QUERY_MAPPING = new HashMap<>();

    public BaseWorkflowQueryController() {
        initQueryMapping();
    }

    private void initQueryMapping() {
        //流程定义
        QUERY_MAPPING.put(ProcessDefinition.class, () -> getProcessEngine()
                .getRepositoryService()
                .createProcessDefinitionQuery());
        //任务
        QUERY_MAPPING.put(Task.class, () -> getProcessEngine()
                .getTaskService()
                .createTaskQuery());
        //活动历史
        QUERY_MAPPING.put(HistoricActivityInstance.class, () -> getProcessEngine()
                .getHistoryService()
                .createHistoricActivityInstanceQuery());
        //历史任务
        QUERY_MAPPING.put(HistoricTaskInstance.class, () -> getProcessEngine()
                .getHistoryService()
                .createHistoricTaskInstanceQuery());
    }

    /**
     * 获取流程服务类
     *
     * @return 默认使用系统默认的流程服务类实现
     */
    protected WorkflowService getProcessService() {
        return workflowService = Optional.ofNullable(workflowService)
                .orElse(SpringUtils.getBean(WorkflowServiceImpl.class, true));
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


    @SneakyThrows
    @Override
    public E get(K key) {
        Query<?, ?> query = getQuery();
        Class<?> queryClass = query.getClass();
        Method method = Optional.ofNullable(ReflectionUtils
                .findMethod(queryClass, getKeyQueryField(), getKeyType()))
                .orElseThrow(()
                        -> new WorkflowException(
                        String.format("cannot found key's field query method by %s,please check class %s",
                                queryClass.getName(),
                                queryClass.getName())
                ));
        method.setAccessible(true);
        method.invoke(query, key);
        return getTransformFunc().apply(query.singleResult());
    }

    /**
     * 获取根据主键查询出实体的字段，如TaskQuery ,那么这里返回的是taskId
     *
     * @return 返回根据主键查询实体的字段
     */
    public abstract String getKeyQueryField();

    /**
     * 获取流程相关的分页数据，如流程定义、任务等
     *
     * @param params   查询参数
     * @param current  当前的页数
     * @param pageSize 分数大小
     * @param <Q>      acvitivi的查询对象
     * @return 分页后的数据
     */
    protected <Q extends Query<Q, ?>> List<E> getPageable(Object params, int current, int pageSize) {
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

        List<?> pageList = query.listPage(current - 1, pageSize);
        return pageList.stream().map(getTransformFunc()).collect(Collectors.toList());
    }

    /**
     * 获取流程引擎对应实体的查询对象
     *
     * @param <Q> 查询对象
     * @return 匹配的查询对象
     */
    @SuppressWarnings("unchecked")
    protected <Q extends Query<Q, ?>> Q getQuery() {
        Class<?> queryEntityInterface = QUERY_MAPPING.keySet().stream().filter(classKey -> classKey.isAssignableFrom(modelClass)).findFirst()
                .orElseThrow(() -> new WorkflowException(String.format("cannot found query's supplier for %s,please overwrite method `getQuery`", modelClass)));
        return (Q) QUERY_MAPPING.get(queryEntityInterface).get();
    }

    /**
     * 转化，用于列表查询后，自定的转化
     *
     * @return 转化的函数
     */
    @SuppressWarnings("unchecked")
    protected Function<? super Object, ? extends E> getTransformFunc() {
        return item -> (E) item;
    }

}
