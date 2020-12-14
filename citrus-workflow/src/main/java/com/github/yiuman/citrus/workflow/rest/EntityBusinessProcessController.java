package com.github.yiuman.citrus.workflow.rest;

import com.github.yiuman.citrus.support.crud.rest.BaseCrudController;
import com.github.yiuman.citrus.support.http.ResponseEntity;
import com.github.yiuman.citrus.system.service.DataRangeService;
import com.github.yiuman.citrus.system.service.RbacMixinService;
import com.github.yiuman.citrus.workflow.model.impl.StartProcessModelImpl;
import com.github.yiuman.citrus.workflow.model.impl.TaskCompleteModelImpl;
import com.github.yiuman.citrus.workflow.service.ProcessQueryService;
import com.github.yiuman.citrus.workflow.service.ProcessService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 实例业务类型的通用流程控制器
 *
 * @author yiuman
 * @date 2020/12/14
 */
public abstract class EntityBusinessProcessController<T, K extends Serializable> extends BaseCrudController<T, K> {

    /**
     * 流程参数，申请用户
     */
    private final static String APPLY_USER_ID = "applyUserId";

    /**
     * 流程参数，业务主键
     */
    private final static String BUSINESS_KEY = "businessKey";

    @Resource
    private ProcessService processService;

    @Resource
    private ProcessQueryService processQueryService;

    @Resource
    private DataRangeService dataRangeService;

    @Resource
    private RbacMixinService rbacMixinService;

    public EntityBusinessProcessController() {
    }

    /**
     * 启动流程
     *
     * @param entity 业务实体
     * @return 流程实例ID
     * @throws Exception 数据库异常或流程异常
     */
    @PostMapping("/process")
    public ResponseEntity<String> startProcess(@RequestBody T entity) throws Exception {
        K key = save(entity);
        Map<String, Object> variables = getVariables(entity);
        ProcessInstance processInstance = processService.starProcess(StartProcessModelImpl.builder()
                .businessKey(key.toString())
                .processDefineId(getProcessDefineKey())
                .variables(variables)
                .userId(variables.get(APPLY_USER_ID).toString()).build()
        );
        return ResponseEntity.ok(processInstance.getId());
    }

    /**
     * 完成任务
     *
     * @param taskId 任务ID
     * @param data   任务的数据
     * @return 空Void
     */
    @PostMapping("/complete/{taskId}")
    public ResponseEntity<Void> complete(@PathVariable String taskId, @RequestBody Map<String, Object> data) {
        processService.complete(TaskCompleteModelImpl.builder()
                .taskId(taskId)
                .taskVariables(data)
                .userId(getCurrentUserId())
                .build());
        return ResponseEntity.ok();
    }

    /**
     * 获取当前用户UUID
     *
     * @return 当前用户的UUID
     */
    protected String getCurrentUserId() {
        return rbacMixinService.getUserService().getCurrentUserOnlineInfo().getUuid();
    }

    /**
     * 获取流程定义key，用于启动流程
     *
     * @return 流程定义key，默认为业务实体的全类名
     */
    protected String getProcessDefineKey() {
        return modelClass.getName();
    }

    /**
     * 根据当前业务实体获取流程变量
     *
     * @param entity 当前的业务实体
     * @return 流程变量
     */
    protected Map<String, Object> getVariables(T entity) {
        K key = getService().getKey(entity);
        Map<String, Object> variables = new HashMap<>();
        variables.put(APPLY_USER_ID, getCurrentUserId());
        variables.put(BUSINESS_KEY, key.toString());
        return variables;
    }
}
