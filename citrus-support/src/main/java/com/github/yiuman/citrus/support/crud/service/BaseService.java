package com.github.yiuman.citrus.support.crud.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.support.crud.mapper.CrudMapper;
import com.github.yiuman.citrus.support.utils.CrudUtils;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 基础的Service
 *
 * @author yiuman
 * @date 2020/4/15
 */
public abstract class BaseService<E, K extends Serializable> implements CrudService<E, K> {

    protected static final Logger log = LoggerFactory.getLogger(BaseService.class);

    public BaseService() {
    }

    /**
     * 获取响应实体的Mapper
     *
     * @return 实体的Mapper
     */
    protected CrudMapper<E> getMapper() {
        try {
            return CrudUtils.getCrudMapper(getEntityType());
        } catch (Throwable throwable) {
            log.info("初始化Mapper失败", throwable);
            return null;
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public K save(E entity) throws Exception {
        if (!this.beforeSave(entity)) {
            return null;
        }
        BaseMapper<E> mapper = getMapper();
        Assert.notNull(mapper, "error: can not execute. because can not find mapper for entity!");

        if (Objects.nonNull(entity)) {
            getMapper().saveEntity(entity);
            //如果找不到主键就直接插入
            this.afterSave(entity);
            return getKey(entity);
        }

        return null;
    }

    @Override
    public boolean batchSave(Iterable<E> entityIterable) {
        return getMapper().saveBatch((Collection<E>) entityIterable);
    }

    @Override
    public K update(E entity) throws Exception {
        if (!this.beforeUpdate(entity)) {
            return null;
        }
        K key = this.save(entity);
        this.afterUpdate(entity);
        return key;
    }

    @Override
    public boolean remove(E entity) {
        return this.beforeRemove(entity) && getMapper().deleteById(getKey(entity)) > 1;
    }

    @Override
    public void batchRemove(Iterable<K> keys) {
        getMapper().deleteBatchIds((Collection<? extends Serializable>) keys);
    }

    @Override
    public void clear() {
        getMapper().deleteBatchIds(
                list().parallelStream()
                        .map(LambdaUtils.functionWrapper(this::getKey))
                        .collect(Collectors.toList()));
    }

    @Override
    public E get(K key) {
        return getMapper().selectById(key);
    }

    @Override
    public E get(Wrapper<E> wrapper) {
        return getMapper().selectOne(wrapper);
    }

    @Override
    public List<E> list() {
        return getMapper().selectList(Wrappers.emptyWrapper());
    }

    @Override
    public List<E> list(Wrapper<E> wrapper) {
        return getMapper().selectList(wrapper);
    }

    @Override
    public <P extends IPage<E>> P page(P page, Wrapper<E> queryWrapper) {
        return getMapper().selectPage(page, queryWrapper);
    }

    @Override
    public boolean remove(Wrapper<E> wrapper) {
        return getMapper().delete(wrapper) >= 0;
    }
}
