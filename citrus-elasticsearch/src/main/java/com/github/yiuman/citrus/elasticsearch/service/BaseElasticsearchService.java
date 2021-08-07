package com.github.yiuman.citrus.elasticsearch.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.TypeUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.elasticsearch.utils.ElasticsearchUtils;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import com.github.yiuman.citrus.support.utils.SpringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.ByQueryResponse;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * todo  待检查实现
 * 基础ES逻辑层
 *
 * @author yiuman
 * @date 2021/8/2
 */
public abstract class BaseElasticsearchService<E, K extends Serializable> implements CrudService<E, K> {

    public BaseElasticsearchService() {
    }

    @SuppressWarnings("unchecked")
    public ElasticsearchRepository<E, K> getRepository() {
        return ElasticsearchUtils.getRepository(
                (Class<E>) TypeUtil.getTypeArgument(getClass(), 0),
                (Class<K>) TypeUtil.getTypeArgument(getClass(), 1)
        );

    }

    public ElasticsearchRestTemplate getElasticsearchRestTemplate() {
        return SpringUtils.getBean(ElasticsearchRestTemplate.class, true);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public K save(E entity) throws Exception {
        if (!this.beforeSave(entity)) {
            return null;
        }
        ElasticsearchRepository<E, K> repository = getRepository();
        Assert.notNull(repository, String.format("error: can not execute. because can not find repository for entity:[%s]", getEntityType().getName()));

        if (Objects.nonNull(entity)) {
            repository.save(entity);
            //如果找不到主键就直接插入
            this.afterSave(entity);
            return getKey(entity);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public K getKey(E entity) {
        return (K) ReflectUtil.getFieldValue(entity, getKeyProperty());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean batchSave(Iterable<E> entityIterable) {
        entityIterable.forEach(LambdaUtils.consumerWrapper(this::beforeSave));
        getRepository().saveAll(entityIterable);
        entityIterable.forEach(LambdaUtils.consumerWrapper(this::afterSave));
        return true;
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
        if (!this.beforeRemove(entity)) {
            return false;
        }
        getRepository().deleteById(getKey(entity));
        return true;
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
        getRepository().deleteAllById(keys);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void clear() {
        getRepository().deleteAll();
    }

    @Override
    public E get(K key) {
        return getRepository().findById(key).orElse(null);
    }

    @Override
    public E get(Wrapper<E> wrapper) {
        ElasticsearchRestTemplate elasticsearchRestTemplate = getElasticsearchRestTemplate();
        return elasticsearchRestTemplate.searchOne(wrapper2Query(wrapper), getEntityType()).getContent();
    }

    /**
     * todo wrapper convert es Query
     * wrapper转Es Query
     *
     * @param wrapper mbp wrapper
     * @return Es'query
     */
    protected Query wrapper2Query(Wrapper<E> wrapper) {
        QueryWrapper<E> queryWrapper = (QueryWrapper<E>) wrapper;
        Map<String, Object> paramNameValuePairs = queryWrapper.getParamNameValuePairs();
        if (CollectionUtil.isEmpty(paramNameValuePairs)) {
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            paramNameValuePairs.forEach((key, value) -> nativeSearchQueryBuilder.withQuery(QueryBuilders.termQuery(key, value)));
            return nativeSearchQueryBuilder.build();
        }
        return null;

    }

    @Override
    public List<E> list() {
        return (List<E>) getRepository().findAll();
    }

    @Override
    public List<E> list(Wrapper<E> wrapper) {
        Stream<E> eStream = getElasticsearchRestTemplate()
                .search(wrapper2Query(wrapper), getEntityType())
                .getSearchHits()
                .stream()
                .map(SearchHit::getContent);
        return eStream.collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <P extends IPage<E>> P page(P page, Wrapper<E> queryWrapper) {
        SearchHits<E> search = getElasticsearchRestTemplate().search(wrapper2Query(queryWrapper), getEntityType());
        P returnPage = (P) new Page<E>();
        returnPage.setCurrent(page.getCurrent());
        returnPage.setPages(page.getPages());
        returnPage.setRecords(search.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList()));
        return returnPage;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean remove(Wrapper<E> wrapper) {
        ByQueryResponse delete = getElasticsearchRestTemplate().delete(wrapper2Query(wrapper), getEntityType());
        return delete.getBatches() > 0;
    }
}
