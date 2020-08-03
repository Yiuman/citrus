package com.github.yiuman.citrus.system.service;

import java.util.Collection;

/**
 * 数据范围服务类
 *
 * @author yiuman
 * @date 2020/7/1
 */
public interface DataRangeService {

    /**
     * 获取数据范围内部门的Id集合
     *
     * @param code 资源代码，若为空则使用处理当前请求的
     * @return 部门的Id集合
     */
    Collection<Long> getDeptIds(String code);

}