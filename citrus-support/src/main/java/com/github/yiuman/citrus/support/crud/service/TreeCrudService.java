package com.github.yiuman.citrus.support.crud.service;

import com.github.yiuman.citrus.support.model.Tree;

import java.io.Serializable;

/**
 * @author yiuman
 * @date 2020/4/15
 */
public interface TreeCrudService<E extends Tree<K>, K extends Serializable> extends TreeOperation<E, K>, CrudService<E, K> {
}