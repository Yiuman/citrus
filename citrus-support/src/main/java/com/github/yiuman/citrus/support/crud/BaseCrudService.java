package com.github.yiuman.citrus.support.crud;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.util.List;

/**
 * 实体CRUD抽象服务类
 *
 * @author yiuman
 * @date 2020/4/4
 */
public abstract class BaseCrudService<M extends BaseMapper<E>, E, K> extends BaseKeyService<M, E, K> implements CrudService<E, K> {

    @Override
    public K saveEntity(E entity) throws Exception {
        save(entity);
        return key(entity);
    }

    @Override
    public void delete(K key) throws Exception {
        removeById((Serializable) key);
    }

    @Override
    public E get(K key) throws Exception {
        return getById((Serializable) key);
    }

    @Override
    public List<E> getList() throws Exception {
        return list();
    }

    @Override
    public <P extends IPage<E>> P selectPage(P page, Wrapper<E> queryWrapper) {
        return getBaseMapper().selectPage(page,queryWrapper);
    }
}
