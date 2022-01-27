package com.github.yiuman.citrus.mda.rest;

import com.github.yiuman.citrus.mda.entity.Table;
import com.github.yiuman.citrus.mda.service.impl.TableEntityServiceImpl;
import com.github.yiuman.citrus.support.crud.rest.BaseCrudController;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 表实体动态模型控制器
 *
 * @author yiuman
 * @date 2021/4/20
 */
@RestController
@RequestMapping("/rest/tables")
public class TableEntityController extends BaseCrudController<Table, String> {

    private final TableEntityServiceImpl tableEntityService;

    public TableEntityController(TableEntityServiceImpl tableEntityService) {
        this.tableEntityService = tableEntityService;
    }

    @Override
    protected CrudService<Table, String> getService() {
        return tableEntityService;
    }

//    @PostMapping("/{uuid}")
//    public ResponseEntity<Void> create(@PathVariable String uuid) throws Exception {
//        tableEntityService.createTable(uuid);
//        return ResponseEntity.ok();
//    }
}
