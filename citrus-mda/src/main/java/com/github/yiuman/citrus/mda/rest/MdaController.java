package com.github.yiuman.citrus.mda.rest;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.github.yiuman.citrus.mda.converter.TableRelUtils;
import com.github.yiuman.citrus.mda.entity.Column;
import com.github.yiuman.citrus.mda.entity.DisplayColumn;
import com.github.yiuman.citrus.mda.service.MdaService;
import com.github.yiuman.citrus.support.crud.rest.BaseCrudController;
import com.github.yiuman.citrus.support.crud.service.CrudService;
import com.github.yiuman.citrus.support.crud.view.impl.PageTableView;
import com.github.yiuman.citrus.support.model.Page;
import org.mvel2.MVEL;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MDA动态模型控制器
 *
 * @author yiuman
 * @date 2021/5/5
 */
@RestController
@RequestMapping("/rest/mda")
public class MdaController extends BaseCrudController<Map<String, Object>, String> {

    private final MdaService mdaService;

    public MdaController(MdaService mdaService) {
        this.mdaService = mdaService;
        setParamClass(HashMap.class);
    }

    @Override
    protected CrudService<Map<String, Object>, String> getService() {
        return mdaService;
    }

    @Override
    public Object showPageView(Page<Map<String, Object>> page) {
        final PageTableView<Map<String, Object>> view = new PageTableView<>();
        List<DisplayColumn> displayColumns = TableRelUtils.getTableRelInfos(mdaService.getModelId(), DisplayColumn.class);
        if (CollectionUtil.isNotEmpty(displayColumns)) {
            displayColumns.forEach(item -> {
                if (!StrUtil.isEmpty(item.getExpression())) {
                    view.addColumn(item.getColumnAlias(), (entity) -> MVEL.eval(item.getExpression(), entity));
                } else {
                    view.addColumn(item.getColumnAlias(), item.getColumnName());
                }
            });
        } else {
            List<Column> columns = TableRelUtils.getTableRelInfos(mdaService.getModelId(), Column.class);
            columns.forEach(column -> view.addColumn(column.getColumnName(), column.getColumnName()));
        }

        return view;
    }


}
