package com.github.yiuman.citrus.support.crud.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.support.crud.CrudHelper;
import com.github.yiuman.citrus.support.crud.mapper.CrudMapper;
import com.github.yiuman.citrus.support.crud.query.Query;
import com.github.yiuman.citrus.support.crud.query.QueryHelper;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * 基础的Service
 *
 * @param <E> 实体类型
 * @param <K> 主键类型
 * @author yiuman
 * @date 2020/4/15
 */
public abstract class BaseService<E, K extends Serializable> implements CrudService<E, K> {

    public BaseService() {
    }

    /**
     * 获取响应实体的Mapper
     *
     * @return 实体的Mapper
     */
    protected CrudMapper<E> getMapper() {
        return CrudHelper.getCrudMapper(getEntityType());
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public K save(E entity) throws Exception {
        if (!this.beforeSave(entity)) {
            return null;
        }
        CrudMapper<E> mapper = getMapper();
        Assert.notNull(mapper, String.format("error: can not execute. because can not find mapper for entity:[%s]", getEntityType().getName()));

        if (Objects.nonNull(entity)) {
            getMapper().saveEntity(entity);
            //如果找不到主键就直接插入
            this.afterSave(entity);
            return getKey(entity);
        }

        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean batchSave(Iterable<E> entityIterable) {
        entityIterable.forEach(LambdaUtils.consumerWrapper(this::beforeSave));
        boolean assertSave = getMapper().saveBatch((Collection<E>) entityIterable);
        if (assertSave) {
            entityIterable.forEach(LambdaUtils.consumerWrapper(this::afterSave));
        }
        return assertSave;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public K update(E entity) throws Exception {
        if (!this.beforeUpdate(entity)) {
            return null;
        }
        K key = this.save(entity);
        this.afterUpdate(entity);
        return key;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean remove(E entity) {
        return this.beforeRemove(entity) && getMapper().deleteById(getKey(entity)) > 1;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchRemove(Iterable<K> keys) {
        List<K> ids = StreamSupport.stream(keys.spliterator(), false).collect(Collectors.toList());
        List<E> list = getMapper().selectList(Wrappers.<E>query().in(getKeyColumn(), ids));
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(this::beforeRemove);
        }
        getMapper().deleteBatchIds((Collection<? extends Serializable>) keys);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void clear() {
        getMapper().deleteBatchIds(list().parallelStream()
                .map(LambdaUtils.functionWrapper(this::getKey))
                .collect(Collectors.toList()));
    }

    @Override
    public E get(K key) {
        return getMapper().selectById(key);
    }

    @Override
    public E get(Query query) {
        return getMapper().selectOne(QueryHelper.getQueryWrapper(query));
    }

    @Override
    public List<E> list() {
        return getMapper().selectList(Wrappers.emptyWrapper());
    }

    @Override
    public List<E> list(Query query) {
        return getMapper().selectList(QueryHelper.getQueryWrapper(query));
    }

    @Override
    public <P extends IPage<E>> P page(P page, Query query) {
        return getMapper().selectPage(page, QueryHelper.getQueryWrapper(query));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean remove(Query query) {
        return getMapper().delete(QueryHelper.getQueryWrapper(query)) >= 0;
    }
}
