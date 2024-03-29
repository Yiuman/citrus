package com.github.yiuman.citrus.worlflow;

import com.github.yiuman.citrus.support.utils.SpringUtils;
import com.github.yiuman.citrus.workflow.cmd.AddMultiInstanceCmd;
import com.github.yiuman.citrus.workflow.model.impl.StartProcessModelImpl;
import com.github.yiuman.citrus.workflow.service.impl.WorkflowServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.collections4.map.HashedMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author yiuman
 * @date 2021/4/8
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Slf4j
public class MultiInstanceTest {

    public MultiInstanceTest() {
    }

    @Test
    public void startProcess() {
        WorkflowServiceImpl workflowService = SpringUtils.getBean(WorkflowServiceImpl.class, true);
        StartProcessModelImpl startProcessModel = StartProcessModelImpl.builder().processDefineKey("test_multiple_claimed")
                .userId("1")
                .variables(new HashedMap<>() {{
                    put("test", "1");
                }}).build();
        ProcessInstance processInstance = workflowService.starProcess(startProcessModel);
        log.info("PROCESSINSTANCEID:{}", processInstance.getProcessInstanceId());
        Assertions.assertNotNull(processInstance);

    }

    @Test
    public void completeTask() {
        String taskId = "8f5cdc68-6e06-11ed-86e9-acde48001122";
        WorkflowServiceImpl workflowService = SpringUtils.getBean(WorkflowServiceImpl.class, true);
        TaskService taskService = workflowService.getProcessEngine().getTaskService();
        taskService.complete(taskId);
    }

    @Test
    public void addMultiInstance() {
        WorkflowServiceImpl workflowService = SpringUtils.getBean(WorkflowServiceImpl.class, true);
        ProcessEngine processEngine = workflowService.getProcessEngine();
        ManagementService managementService = processEngine.getManagementService();

        managementService.executeCommand(AddMultiInstanceCmd.builder()
                .assignee("加签（1）")
                .taskId("6b6f6ed0-982c-11eb-8e7a-9a439eeb28db").build());
    }
}
