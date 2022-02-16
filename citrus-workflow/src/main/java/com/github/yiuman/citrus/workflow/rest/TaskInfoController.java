package com.github.yiuman.citrus.workflow.rest;

import com.github.yiuman.citrus.support.crud.view.impl.PageTableView;
import com.github.yiuman.citrus.workflow.vo.TaskInfoVo;
import com.github.yiuman.citrus.workflow.vo.TaskQueryParams;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Function;

/**
 * 任务查询控制器
 *
 * @author yiuman
 * @date 2021/3/8
 */
@RestController
@RequestMapping("/rest/tasks")
public class TaskInfoController extends BaseWorkflowQueryController<TaskInfoVo, String> {

    public TaskInfoController() {
        setParamClass(TaskQueryParams.class);
    }

    @Override
    public Object createPageView() {
        PageTableView<TaskInfoVo> view = new PageTableView<>();
        view.addWidget("处理人或候选人", "taskCandidateOrAssigned");
        view.addColumn("任务名称", "name");
        view.addColumn("处理人", "assignee");
        view.addColumn("创建时间", "createTime");
        view.addColumn("处理时间", "dueDate");
        view.addColumn("删除原因", "deleteReason");
        return view;
    }

    @Override
    public String getKeyQueryField() {
        return "taskId";
    }

    @Override
    protected Function<? super Object, ? extends TaskInfoVo> getTransformFunc() {
        return task -> {
            TaskInfoVo taskInfo = new TaskInfoVo();
            BeanUtils.copyProperties(task, taskInfo);
            return taskInfo;
        };
    }
}
