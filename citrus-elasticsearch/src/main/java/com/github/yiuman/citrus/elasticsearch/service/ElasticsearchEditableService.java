package com.github.yiuman.citrus.elasticsearch.service;

import com.github.yiuman.citrus.support.crud.service.KeyBasedService;

import java.io.Serializable;

/**
 * @author yiuman
 * @date 2021/8/10
 */
public interface ElasticsearchEditableService<E, K extends Serializable> extends
        ElasticsearchDeletableService<E, K>,
        ElasticsearchSelectService<E, K>,
        KeyBasedService<E, K> {
}