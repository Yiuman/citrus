package com.github.yiuman.citrus.workflow.service;

import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.workflow.model.ProcessBusinessModel;

import java.io.Serializable;

/**
 * 集成CRUD与流程相关的顶层接口
 *
 * @author yiuman
 * @date 2020/12/16
 */
public interface EntityCrudProcessService<E extends ProcessBusinessModel, K extends Serializable>
        extends CrudService<E, K>, EntityProcessService<E> {
}