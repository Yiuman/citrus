package com.github.yiuman.citrus.workflow.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yiuman.citrus.support.crud.service.BaseService;
import com.github.yiuman.citrus.workflow.exception.WorkflowException;
import com.github.yiuman.citrus.workflow.model.ProcessBusinessModel;
import com.github.yiuman.citrus.workflow.model.StartProcessModel;
import com.github.yiuman.citrus.workflow.model.TaskCompleteModel;
import com.github.yiuman.citrus.workflow.model.impl.StartProcessModelImpl;
import com.github.yiuman.citrus.workflow.model.impl.TaskCompleteModelImpl;
import com.github.yiuman.citrus.workflow.service.EntityCrudWorkflowService;
import com.github.yiuman.citrus.workflow.service.WorkflowService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.runtime.ProcessInstance;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 实体业务模型的基础流程逻辑服务类
 *
 * @author yiuman
 * @date 2020/12/14
 */
@Slf4j
public abstract class BaseEntityWorkflowService<E extends ProcessBusinessModel, K extends Serializable>
        extends BaseService<E, K> implements EntityCrudWorkflowService<E, K> {

    /**
     * 流程参数，申请用户
     */
    private final static String APPLY_USER_ID = "applyUserId";

    /**
     * 流程参数，业务主键
     */
    private final static String BUSINESS_KEY = "businessKey";

    /**
     * 流程服务类
     */
    private WorkflowService processService;

    /**
     * 获取流程服务类
     *
     * @return 默认使用系统默认的流程服务类实现
     */
    protected WorkflowService getProcessService() {
        return processService = Optional.ofNullable(processService)
                .orElse(new WorkflowServiceImpl());
    }

    /**
     * 开启流程
     *
     * @param entity 实体
     * @return 流程实例
     * @throws Exception 数据库操作异常
     */
    @Override
    public ProcessInstance starProcess(E entity) throws Exception {
        K key = save(entity);
        String instanceId = entity.getProcessInstanceId();
        //已经有流程实例ID
        if (StringUtils.isNotBlank(instanceId)) {
            throw new WorkflowException("This Business Process has been start!");
        }
        Map<String, Object> variables = getVariables(entity);
        ProcessInstance processInstance = starProcess(StartProcessModelImpl.builder()
                .businessKey(key.toString())
                .processDefineId(getProcessDefineKey())
                .variables(variables)
                .userId(variables.get(APPLY_USER_ID).toString()).build()
        );
        entity.setProcessInstanceId(processInstance.getProcessInstanceId());
        update(entity);
        return processInstance;
    }

    /**
     * 获取流程定义key，用于启动流程
     *
     * @return 流程定义key，默认为业务实体的全类名
     */
    @Override
    public String getProcessDefineKey() {
        return getEntityType().getName();
    }

    /**
     * 根据当前业务实体获取流程变量
     *
     * @param entity 当前的业务实体
     * @return 流程变量
     */
    protected Map<String, Object> getVariables(E entity) {
        K key = getKey(entity);
        Map<String, Object> variables = new HashMap<>();
        variables.put(APPLY_USER_ID, getCurrentUserId());
        variables.put(BUSINESS_KEY, key.toString());
        return variables;
    }

    @Override
    public void complete(String taskId, Map<String, Object> variables) {
        complete(TaskCompleteModelImpl.builder()
                .taskId(taskId)
                .taskVariables(variables)
                .userId(getCurrentUserId())
                .build());
    }

    protected abstract String getCurrentUserId();

    @Override
    public ProcessInstance starProcess(StartProcessModel model) {
        return getProcessService().starProcess(model);
    }


    @Override
    public void complete(TaskCompleteModel model) {
        getProcessService().complete(model);
    }

    @Override
    public void claim(String taskId, String userId) {
        getProcessService().claim(taskId, userId);
    }

    @Override
    public void suspend(String instanceId) {
        getProcessService().suspend(instanceId);
    }

    @Override
    public void activate(String instanceId) {
        getProcessService().activate(instanceId);
    }
}
