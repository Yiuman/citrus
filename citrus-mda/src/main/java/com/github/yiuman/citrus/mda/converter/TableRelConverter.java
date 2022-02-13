package com.github.yiuman.citrus.mda.converter;

import com.github.yiuman.citrus.mda.entity.TableRel;
import com.github.yiuman.citrus.mda.exception.ConverterException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 来源实体与表实体有关系的实体表的转化器
 * 比如Column,HistoryTable等
 *
 * @param <S> 表格关系
 * @param <R> 返回类型
 * @author yiuman
 * @date 2021/4/29
 */
public interface TableRelConverter<S extends TableRel, R> extends Converter<S, R> {

    /**
     * 转换列表
     *
     * @param tableUuid 表的uuid
     * @return 目标列表
     * @throws ConverterException 转化异常
     */
    default List<R> convertList(String tableUuid) throws ConverterException {
        return TableRelUtils.getTableRelInfos(tableUuid, getSourceClass())
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }


}