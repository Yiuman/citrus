package com.github.yiuman.citrus.mda.service;

import com.github.yiuman.citrus.mda.entity.Table;
import com.github.yiuman.citrus.mda.entity.history.HistoryTable;
import com.github.yiuman.citrus.support.crud.service.CrudService;

/**
 * 表实体逻辑服务类
 *
 * @author yiuman
 * @date 2021/5/2
 */
public interface TableEntityService extends CrudService<Table, String> {


    /**
     * 实体表转为历史表
     *
     * @param table 实体表
     * @return 历史表
     */
    HistoryTable entity2History(Table table);
}