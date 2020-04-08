package com.github.yiuman.citrus.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yiuman.citrus.support.model.Tree;

/**
 * @author yiuman
 * @date 2020/4/7
 */
public interface BaseTreeMapper<T extends Tree<K>, K> extends BaseMapper<T> {

}
