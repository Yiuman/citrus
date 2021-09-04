package com.github.yiuman.citrus.workflow.service;

import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.workflow.model.ProcessBusinessModel;

import java.io.Serializable;

/**
 * 集成CRUD与流程相关的顶层接口
 *
 * @param <E> 实体类型
 * @param <K> 主键类型
 * @author yiuman
 * @date 2020/12/16
 */
public interface EntityCrudWorkflowService<E extends ProcessBusinessModel, K extends Serializable>
        extends CrudService<E, K>, EntityWorkflowService<E> {
}