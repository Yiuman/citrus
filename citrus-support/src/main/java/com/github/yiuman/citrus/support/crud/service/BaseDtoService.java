package com.github.yiuman.citrus.support.crud.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yiuman.citrus.support.crud.mapper.CrudMapper;
import com.github.yiuman.citrus.support.utils.ConvertUtils;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 复杂实体类型（需传输转化的逻辑基类）
 *
 * @author yiuman
 * @date 2020/4/15
 */
@Slf4j
public abstract class BaseDtoService<E, K extends Serializable, D> implements CrudService<D, K> {

    private final Class<D> dtoClass = currentDtoClass();

    protected BaseService<E, K> getService() {
        return CrudHelper.getCrudService(getClass());
    }

    /**
     * Mybatis Mapper
     *
     * @return Mybatis Mapper
     */
    protected CrudMapper<E> getBaseMapper() {
        return getService().getMapper();
    }

    @SuppressWarnings("unchecked")
    private Class<D> currentDtoClass() {
        return (Class<D>) ReflectionKit.getSuperClassGenericType(getClass(), 2);
    }

    protected Function<D, E> dtoToEntity() {
        return LambdaUtils.functionWrapper(d -> {
            E e = getService().getEntityType().newInstance();
            BeanUtils.copyProperties(d, e);
            return e;
        });
    }

    protected Function<E, D> entityToDto() {
        return LambdaUtils.functionWrapper(e -> {
            D d = dtoClass.newInstance();
            BeanUtils.copyProperties(e, d);
            return d;
        });
    }

    public E getRealEntity(K key) {
        return dtoToEntity().apply(get(key));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public K save(D entity) throws Exception {
        if (!beforeSave(entity)) {
            return null;
        }
        K key = getService().save(dtoToEntity().apply(entity));
        setKey(entity, key);
        afterSave(entity);
        return key;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean batchSave(Iterable<D> entityIterable) {
        entityIterable.forEach(LambdaUtils.consumerWrapper(this::beforeSave));
        final List<E> entityList = new ArrayList<>();
        entityIterable.forEach(dto -> entityList.add(dtoToEntity().apply(dto)));
        boolean assertSave = getBaseMapper().saveBatch(entityList);
        if (assertSave) {
            entityList.stream()
                    .map(realEntity -> entityToDto().apply(realEntity))
                    .collect(Collectors.toList())
                    .forEach(LambdaUtils.consumerWrapper(this::afterSave));
        }
        return assertSave;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public K update(D entity) throws Exception {
        if (!beforeUpdate(entity)) {
            return null;
        }
        K key = save(entity);
        setKey(entity, key);
        afterUpdate(entity);
        return key;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean remove(D entity) {
        return beforeRemove(entity) && getService().remove(dtoToEntity().apply(entity));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Wrapper<D> wrapper) {
        assertWrapper(wrapper);
        return getService().remove((Wrapper<E>) wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchRemove(Iterable<K> keys) {
        List<K> keyList = new ArrayList<>();
        keys.forEach(keyList::add);
        List<D> list = list(Wrappers.<D>query().in(getKeyColumn(), keyList));
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(this::beforeRemove);
        }
        getService().batchRemove(keys);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void clear() {
        getService().clear();
    }

    @Override
    public D get(K key) {
        return entityToDto().apply(getService().get(key));
    }

    @SuppressWarnings("unchecked")
    @Override
    public D get(Wrapper<D> wrapper) {
        assertWrapper(wrapper);
        return entityToDto().apply(getBaseMapper().selectOne((Wrapper<E>) wrapper));
    }

    @Override
    public List<D> list() {
        return ConvertUtils.listConvert(dtoClass, getService().list());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<D> list(Wrapper<D> wrapper) {
        assertWrapper(wrapper);
        return ConvertUtils.listConvert(dtoClass, getService().list((Wrapper<E>) wrapper));
    }

    @Override
    public <P extends IPage<D>> P page(P page, Wrapper<D> queryWrapper) {
        assertWrapper(queryWrapper);
        //拷贝
        Page<E> entityPage = new Page<>();
        BeanUtils.copyProperties(page, entityPage);
        getService().page(entityPage, (QueryWrapper<E>) queryWrapper);
        //反拷贝
        BeanUtils.copyProperties(entityPage, page);
        page.setRecords(ConvertUtils.listConvert(dtoClass, entityPage.getRecords()));
        return page;
    }

    @Override
    public void setKey(D entity, K key) throws Exception {
        Field field = ReflectionUtils.findField(dtoClass, getService().getKeyProperty());
        field.setAccessible(true);
        field.set(entity, key);
    }

    /**
     * 断言当前的Wrapper是否使用了Lambda的实现，如果是则抛出异常。
     * 因为DTO并没有映射数据库表信息，Lambda实现会反射找到DTO实体类对应的数据表信息，找不到会出异常
     * 所以DTO服务类中使用普通的wrapper
     *
     * @param wrapper Wrapper
     */
    private void assertWrapper(Wrapper<D> wrapper) {
        if (wrapper instanceof AbstractLambdaWrapper) {
            throw new RuntimeException(String.format("DTO's service class cannot use LambdaWrapper:[%s]", wrapper.getClass()));
        }
    }

}
