package com.github.yiuman.citrus.support.crud.service;

import java.io.Serializable;

/**
 * 实体增删改查Service
 *
 * @author yiuman
 * @date 2020/4/15
 */
public interface CrudService<E, K extends Serializable> extends
        EditableService<E, K>, KeyBasedService<E, K>,
        EditSurround<E>, DeleteSurround<E> {

}