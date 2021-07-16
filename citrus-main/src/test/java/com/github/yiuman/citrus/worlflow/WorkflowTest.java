package com.github.yiuman.citrus.worlflow;

import com.github.yiuman.citrus.support.utils.SpringUtils;
import com.github.yiuman.citrus.workflow.model.impl.StartProcessModelImpl;
import com.github.yiuman.citrus.workflow.model.impl.TaskCompleteModelImpl;
import com.github.yiuman.citrus.workflow.service.impl.WorkflowServiceImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.collections4.map.HashedMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author yiuman
 * @date 2021/4/6
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class WorkflowTest {

    /**
     * # 测试任务跳转相关查询
     * # 查询流程定义
     * select *
     * from act_re_procdef;
     * <p>
     * # 根据定义id查询流程实例
     * select *
     * from act_hi_procinst
     * where PROC_DEF_ID_ = 'test_task_jump:1:75e9971d-96b2-11eb-b265-863668d795e3';
     * <p>
     * # 根据流程实例ID查询任务
     * select *
     * from act_ru_task
     * where PROC_INST_ID_ = '360790c8-96be-11eb-962f-863668d795e3';
     * <p>
     * #根据流程实例ID查询历史活动
     * select *
     * from act_hi_actinst
     * where PROC_INST_ID_ = '360790c8-96be-11eb-962f-863668d795e3' order by START_TIME_ ;
     * <p>
     * #根据流程实例查询流程变量
     * select * from act_hi_varinst where PROC_INST_ID_ = '360790c8-96be-11eb-962f-863668d795e3'
     * and TASK_ID_ ='cd6e1d1f-96b3-11eb-aa07-863668d795e3';
     * <p>
     * #根据流程实例查询执行实例
     * select * from act_ru_execution where PROC_INST_ID_ = '360790c8-96be-11eb-962f-863668d795e3';
     */

    public WorkflowTest() {
    }

    @Test
    public void startProcess() {
        WorkflowServiceImpl workflowService = SpringUtils.getBean(WorkflowServiceImpl.class, true);
        StartProcessModelImpl startProcessModel = StartProcessModelImpl.builder().processDefineKey("test_task_jump")
                .userId("1")
                .variables(new HashedMap<String, Object>() {{
                    put("test", "1");
                }}).build();
        ProcessInstance processInstance = workflowService.starProcess(startProcessModel);
        Assertions.assertNotNull(processInstance);

    }

    /**
     * 简单的任务完成
     */
    @Test
    public void completeTask() {
        TaskCompleteModelImpl taskCompleteModel = TaskCompleteModelImpl.builder()
                .userId("1")
                .taskId("")
                .build();
        WorkflowServiceImpl workflowService = new WorkflowServiceImpl();
        workflowService.complete(taskCompleteModel);
    }

    /**
     * 任务跳转
     */
    @Test
    public void completeAndJump() {
        TaskCompleteModelImpl taskCompleteModel = TaskCompleteModelImpl.builder()
                .userId("1")
                //当前任务ID (test_task_jump中的领导1审批)
                .taskId("78aa2386-96c2-11eb-9fed-863668d795e3")
                //需跳转到的任务定义KEY,(test_task_jump中的请假申请)
                .targetTaskKey("Activity_1s4evtd")
                .build();
        WorkflowServiceImpl workflowService = new WorkflowServiceImpl();
        workflowService.complete(taskCompleteModel);
    }
}
