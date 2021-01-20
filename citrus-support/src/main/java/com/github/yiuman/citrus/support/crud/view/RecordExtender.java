package com.github.yiuman.citrus.support.crud.view;

import java.util.Map;
import java.util.function.Function;

/**
 * 记录扩展器，用于扩展记录
 *
 * @author yiuman
 * @date 2021/1/20
 */
public interface RecordExtender<T> extends Function<T, Map<String, Object>> {

}