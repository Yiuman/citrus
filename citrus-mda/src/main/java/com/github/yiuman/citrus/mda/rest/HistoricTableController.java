package com.github.yiuman.citrus.mda.rest;

import com.github.yiuman.citrus.mda.entity.history.HistoryTable;
import com.github.yiuman.citrus.support.crud.rest.BaseCrudController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yiuman
 * @date 2021/4/30
 */
@RestController
@RequestMapping("/rest/history_tables")
public class HistoricTableController extends BaseCrudController<HistoryTable, String> {

    public HistoricTableController() {
    }
}
