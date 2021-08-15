package com.github.yiuman.citrus.elasticsearch.service;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.yiuman.citrus.support.crud.query.Operations;
import com.github.yiuman.citrus.support.crud.query.Query;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import com.github.yiuman.citrus.support.utils.SpringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.ByQueryResponse;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 基础ES逻辑层
 *
 * @author yiuman
 * @date 2021/8/2
 */
public abstract class BaseElasticsearchService<E, K extends Serializable> implements CrudService<E, K> {

    private Field idField;

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
        //todo
    }

    @Override
    public void clear() {
        getElasticsearchRestTemplate().delete(org.springframework.data.elasticsearch.core.query.Query.findAll(), getEntityType());
    }

    @Override
    public E get(K key) {
        ElasticsearchRestTemplate elasticsearchRestTemplate = getElasticsearchRestTemplate();
        return elasticsearchRestTemplate.get(key.toString(), getEntityType());
    }

    @Override
    public E get(Query query) {
        ElasticsearchRestTemplate elasticsearchRestTemplate = getElasticsearchRestTemplate();
        return elasticsearchRestTemplate.searchOne(buildElasticsearchQuery(query), getEntityType()).getContent();
    }


    @Override
    public List<E> list() {
        SearchHits<E> search = getElasticsearchRestTemplate().search(org.springframework.data.elasticsearch.core.query.Query.findAll(), getEntityType());
        if (CollectionUtil.isEmpty(search)) {
            return null;
        }

        return search.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    @Override
    public List<E> list(Query query) {
        Stream<E> eStream = getElasticsearchRestTemplate()
                .search(buildElasticsearchQuery(query), getEntityType())
                .getSearchHits()
                .stream()
                .map(SearchHit::getContent);
        return eStream.collect(Collectors.toList());
    }

    @Override
    public <P extends IPage<E>> P page(P page, Query query) {
        ElasticsearchRestTemplate elasticsearchRestTemplate = getElasticsearchRestTemplate();
        org.springframework.data.elasticsearch.core.query.Query elasticsearchQuery = buildElasticsearchQuery(query);
        elasticsearchQuery.setPageable(PageRequest.of((int) page.getCurrent(), (int) page.getSize()));
        SearchHits<E> search = elasticsearchRestTemplate
                .search(elasticsearchQuery, getEntityType());
        Page<E> returnPage = new Page<>();
        returnPage.setCurrent(page.getCurrent());
        returnPage.setPages(page.getPages());
        returnPage.setTotal(search.getTotalHits());
        returnPage.setRecords(search.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList()));
        returnPage.setItemKey(getKeyProperty());
        //noinspection unchecked
        return (P) returnPage;
    }

    protected org.springframework.data.elasticsearch.core.query.Query buildElasticsearchQuery(Query query) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (Objects.nonNull(query) && CollUtil.isNotEmpty(query.getConditions())) {
            query.getConditions().forEach(conditionInfo -> {
                if (Operations.EQ.getType().equals(conditionInfo.getOperator())) {
                    boolQueryBuilder.must(QueryBuilders.termQuery(conditionInfo.getMapping(), conditionInfo.getValue()));
                }

                if (Operations.LIKE.getType().equals(conditionInfo.getOperator())) {
                    boolQueryBuilder.must(QueryBuilders.matchQuery(conditionInfo.getMapping(), conditionInfo.getValue()));
                }
            });

        } else {
            boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        }

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder);

        if (CollUtil.isNotEmpty(query.getSorts())) {
            query.getSorts().forEach(sortBy -> nativeSearchQueryBuilder
                    .withSort(SortBuilders.fieldSort(sortBy.getSortBy())
                            .order(sortBy.getSortDesc() ? SortOrder.DESC : SortOrder.ASC)));
        } else {
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(getKeyColumn()).order(SortOrder.ASC));
        }

        return nativeSearchQueryBuilder.build();
    }

    @Override
    public boolean remove(Query query) {
        ByQueryResponse delete = getElasticsearchRestTemplate().delete(buildElasticsearchQuery(query), getEntityType());
        return delete.getBatches() > 0;
    }

    @Override
    public String getKeyProperty() {
        if (Objects.nonNull(idField)) {
            return idField.getName();
        }

        Field[] fields = ReflectUtil.getFields(getEntityType());
        idField = Arrays.stream(fields).filter(field -> {
            field.setAccessible(true);
            return Objects.nonNull(AnnotationUtil.getAnnotation(field, Id.class));
        }).findFirst().orElseThrow(() -> new RuntimeException(String.format("cannot find id for entity:%s", getEntityType())));
        return idField.getName();
    }

    @Override
    public String getKeyColumn() {
        if (Objects.isNull(idField)) {
            getKeyProperty();
        }

        org.springframework.data.elasticsearch.annotations.Field fieldAnnotation
                = AnnotationUtil.getAnnotation(idField, org.springframework.data.elasticsearch.annotations.Field.class);
        if (Objects.nonNull(fieldAnnotation)) {
            return fieldAnnotation.value();
        }

        return getKeyProperty();
    }
}
