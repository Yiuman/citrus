package com.github.yiuman.citrus.support.crud.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.support.crud.CrudHelper;
import com.github.yiuman.citrus.support.crud.mapper.CrudMapper;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
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
        List<K> keyList = new ArrayList<>();
        keys.forEach(keyList::add);
        List<E> list = list(Wrappers.<E>query().in(getKeyColumn(), keyList));
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean remove(Wrapper<E> wrapper) {
        return getMapper().delete(wrapper) >= 0;
    }
}
