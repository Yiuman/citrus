package com.github.yiuman.citrus.workflow.service;

import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import io.jsonwebtoken.lang.Assert;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.HashMap;
import java.util.Map;

/**
 * 流程抽象类<br/>
 * 泛型F代表开始流程的业务表单类型。
 *
 * @author yiuman
 * @date 2020/12/11
 */
public abstract class BaseProcessService<F> extends BaseFlowableService implements ProcessService<F> {

    protected Class<F> formClass = currentFormModelClass();

    @SuppressWarnings("unchecked")
    private Class<F> currentFormModelClass() {
        return (Class<F>) ReflectionKit.getSuperClassGenericType(getClass(), 0);
    }

    @Override
    public String getProcessDefinitionKey() {
        return formClass.getSimpleName();
    }

    @Override
    public ProcessInstance starProcess(F form) {

        //找到流程定义
        ProcessDefinition definition = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionId(getProcessDefinitionKey()).singleResult();

        Assert.notNull(definition, String.format("can not find ProcessDefinition for key:[%s]", getProcessDefinitionKey()));

        //开起流程
        return runtimeService.startProcessInstanceById(
                definition.getId(),
                String.format(getProcessDefinitionKey() + ":%s", getBusinessFormKey(form)),
                getProcessInstanceVars(form)
        );
    }

    @Override
    public Map<String, Object> getProcessInstanceVars(F form) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("BusinessClass", form.getClass().toString());
        variables.put("BusinessKey", String.format(getProcessDefinitionKey() + ":%s", getBusinessFormKey(form)));
        variables.put("ApplyUserId", String.format(getProcessDefinitionKey() + ":%s", getBusinessFormKey(form)));
        return variables;
    }

    @Override
    public void suspend(String instanceId) {
        runtimeService.suspendProcessInstanceById(instanceId);
    }

    @Override
    public void activate(String instanceId) {
        runtimeService.activateProcessInstanceById(instanceId);
    }
}