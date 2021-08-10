package com.github.yiuman.citrus.elasticsearch.service;

import com.github.yiuman.citrus.support.crud.service.DeleteSurround;
import com.github.yiuman.citrus.support.crud.service.EditSurround;
import com.github.yiuman.citrus.support.crud.service.KeyBasedService;

import java.io.Serializable;

/**
 * Elasticsearch逻辑层接口
 *
 * @author yiuman
 * @date 2021/8/10
 */
public interface ElasticsearchCrudService<E, K extends Serializable> extends
        ElasticsearchEditableService<E, K>,
        KeyBasedService<E, K>,
        EditSurround<E>,
        DeleteSurround<E> {
}