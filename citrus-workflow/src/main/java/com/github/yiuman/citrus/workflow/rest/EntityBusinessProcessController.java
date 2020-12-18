package com.github.yiuman.citrus.workflow.rest;

import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.github.yiuman.citrus.support.crud.rest.BaseCrudController;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.http.ResponseEntity;
import com.github.yiuman.citrus.support.utils.CrudUtils;
import com.github.yiuman.citrus.workflow.model.ProcessBusinessModel;
import com.github.yiuman.citrus.workflow.service.impl.BaseEntityWorkflowService;
import com.github.yiuman.citrus.workflow.service.EntityCrudWorkflowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.util.Map;

/**
 * 实例业务类型的通用流程控制器
 *
 * @author yiuman
 * @date 2020/12/14
 */
@Slf4j
public abstract class EntityBusinessProcessController<E extends ProcessBusinessModel, K extends Serializable>
        extends BaseCrudController<E, K> {

    public EntityBusinessProcessController() {
    }

    @SuppressWarnings("unchecked")
    protected EntityCrudWorkflowService<E, K> getProcessService() {
        try {
            return CrudUtils.getCrudService(
                    modelClass,
                    (Class<K>) ReflectionKit.getSuperClassGenericType(getClass(), 1),
                    BaseEntityWorkflowService.class);
        } catch (Exception e) {
            log.info("获取EntityCrudProcessService报错", e);
            return null;
        }
    }

    @Override
    protected CrudService<E, K> getService() {
        return getProcessService();
    }

    /**
     * 启动流程
     *
     * @param entity 业务实体
     * @return 流程实例ID
     * @throws Exception 数据库异常或流程异常
     */
    @PostMapping("/process")
    public ResponseEntity<String> startProcess(@RequestBody E entity) throws Exception {
        return ResponseEntity.ok(getProcessService().starProcess(entity).getId());
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
        getProcessService().complete(taskId, data);
        return ResponseEntity.ok();
    }

}
