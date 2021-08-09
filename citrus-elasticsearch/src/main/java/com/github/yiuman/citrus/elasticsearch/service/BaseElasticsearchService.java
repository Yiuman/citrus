package com.github.yiuman.citrus.elasticsearch.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.NormalSegmentList;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import com.github.yiuman.citrus.support.utils.SpringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.ByQueryResponse;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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

    public ElasticsearchRestTemplate getElasticsearchRestTemplate() {
        return SpringUtils.getBean(ElasticsearchRestTemplate.class, true);
    }

    @Override
    public K save(E entity) throws Exception {
        if (!this.beforeSave(entity)) {
            return null;
        }

        ElasticsearchRestTemplate elasticsearchRestTemplate = getElasticsearchRestTemplate();
        if (Objects.nonNull(entity)) {
            elasticsearchRestTemplate.save(entity);
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

    @Override
    public boolean batchSave(Iterable<E> entityIterable) {
        ElasticsearchRestTemplate elasticsearchRestTemplate = getElasticsearchRestTemplate();
        entityIterable.forEach(LambdaUtils.consumerWrapper(this::beforeSave));
        elasticsearchRestTemplate.save(entityIterable);
        entityIterable.forEach(LambdaUtils.consumerWrapper(this::afterSave));
        return true;
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
        if (!this.beforeRemove(entity)) {
            return false;
        }

        getElasticsearchRestTemplate().delete(getKey(entity));
        return true;
    }

    @Override
    public void batchRemove(Iterable<K> keys) {
        List<K> keyList = new ArrayList<>();
        keys.forEach(keyList::add);
        List<E> list = list(Wrappers.<E>query().in(getKeyColumn(), keyList));

        if (CollectionUtil.isNotEmpty(list)) {
            ElasticsearchRestTemplate elasticsearchRestTemplate = getElasticsearchRestTemplate();
            list.forEach(entity -> {
                this.beforeRemove(entity);
                elasticsearchRestTemplate.delete(entity);
            });
        }
    }

    @Override
    public void clear() {
        getElasticsearchRestTemplate().delete(Query.findAll(), getEntityType());
    }

    @Override
    public E get(K key) {
        ElasticsearchRestTemplate elasticsearchRestTemplate = getElasticsearchRestTemplate();
        return elasticsearchRestTemplate.get(key.toString(), getEntityType());
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
        //这个是普通查询
        NormalSegmentList normal = queryWrapper.getExpression().getNormal();
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        if (CollectionUtil.isNotEmpty(normal)) {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//            normal.forEach(queryItem -> {
//                boolQueryBuilder.must(QueryBuilders.termQuery(queryItem, value))
//            });
        }

        return nativeSearchQueryBuilder.build();

    }

    @Override
    public List<E> list() {
        SearchHits<E> search = getElasticsearchRestTemplate().search(Query.findAll(), getEntityType());
        if (CollectionUtil.isEmpty(search)) {
            return null;
        }

        return search.stream().map(SearchHit::getContent).collect(Collectors.toList());
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
        ElasticsearchRestTemplate elasticsearchRestTemplate = getElasticsearchRestTemplate();
        SearchHits<E> search = elasticsearchRestTemplate
                .search(wrapper2Query(queryWrapper), getEntityType());
        P returnPage = (P) new Page<E>();
        returnPage.setCurrent(page.getCurrent());
        returnPage.setPages(page.getPages());
        returnPage.setRecords(search.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList()));
        return returnPage;
    }

    @Override
    public boolean remove(Wrapper<E> wrapper) {
        ByQueryResponse delete = getElasticsearchRestTemplate().delete(wrapper2Query(wrapper), getEntityType());
        return delete.getBatches() > 0;
    }
}
