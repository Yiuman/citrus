package com.github.yiuman.citrus.workflow.rest;

import com.github.yiuman.citrus.support.crud.view.impl.PageTableView;
import org.activiti.engine.history.HistoricActivityInstance;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 流程历史活动控制器
 *
 * @author yiuman
 * @date 2021/3/11
 */
@RestController
@RequestMapping("/rest/activities")
public class ActivityController extends BaseWorkflowQueryController<HistoricActivityInstance, String> {

    public ActivityController() {
    }

    @Override
    protected Object createView(List<HistoricActivityInstance> records) {
        PageTableView<HistoricActivityInstance> view = new PageTableView<>();
        view.addColumn("ID", "activityId");
        view.addColumn("活动名称", "activityName");
        view.addColumn("类型", "activityType");
        view.addColumn("开始时间", "startTime");
        view.addColumn("结束时间", "endTime");
        return view;
    }

    @Override
    public String getKeyQueryField() {
        return "activityInstanceId";
    }
}
