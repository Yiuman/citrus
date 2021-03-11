package com.github.yiuman.citrus.workflow.rest;

import com.github.yiuman.citrus.support.crud.view.impl.PageTableView;
import com.github.yiuman.citrus.workflow.vo.HistoricTaskVo;
import com.github.yiuman.citrus.workflow.vo.TaskQueryParams;
import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Function;

/**
 * 历史任务控制器
 *
 * @author yiuman
 * @date 2021/3/11
 */
@RestController
@RequestMapping("/rest/history_tasks")
public class TaskHistoryController extends BaseWorkflowQueryController<HistoricTaskVo, String> {

    public TaskHistoryController() {
        setParamClass(TaskQueryParams.class);
    }

    @Override
    protected Object createView() {
        PageTableView<HistoricTaskInstance> view = new PageTableView<>();
        view.addWidget("处理人或候选人", "taskCandidateOrAssigned");
        view.addHeader("任务名称", "name");
        view.addHeader("处理人", "assignee");
        view.addHeader("创建时间", "createTime");
        view.addHeader("结束时间", "endTime");
        return view;
    }

    @Override
    public String getKeyQueryField() {
        return "taskId";
    }

    @Override
    protected Function<? super Object, ? extends HistoricTaskVo> getTransformFunc() {
        return historicTaskInstance -> {
            HistoricTaskVo historicTaskVo = new HistoricTaskVo();
            BeanUtils.copyProperties(historicTaskInstance, historicTaskVo);
            return historicTaskVo;
        };
    }
}
