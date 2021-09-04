package com.github.yiuman.citrus.support.crud;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据读取监听器
 *
 * @param <T> 实体类型
 * @param <K> 主键类型
 * @author yiuman
 * @date 2020/4/8
 */
@Slf4j
public class CrudReadDataListener<T, K extends Serializable> extends AnalysisEventListener<T> {

    private static final int BATCH_COUNT = 3000;

    private final List<T> list = new ArrayList<>();

    private final CrudService<T, K> service;

    public CrudReadDataListener(CrudService<T, K> service) {
        this.service = service;
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        list.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
    }

    private void saveData() {
        try {
            service.batchSave(list);
        } catch (Exception e) {
            log.error("导入数据报错", e);
        }

    }
}
