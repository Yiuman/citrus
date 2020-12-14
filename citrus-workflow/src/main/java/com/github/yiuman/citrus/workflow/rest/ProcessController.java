package com.github.yiuman.citrus.workflow.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yiuman.citrus.support.crud.rest.BaseQueryController;
import com.github.yiuman.citrus.support.model.Page;
import com.github.yiuman.citrus.workflow.model.impl.StartProcessModelImpl;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 任务相关Rest控制器
 *
 * @author yiuman
 * @date 2020/12/10
 */
@RestController
@RequestMapping("/rest/workflow_task")
public class ProcessController extends BaseQueryController<Task, String> {

    private final TaskService taskService;

    public ProcessController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    protected Page<Task> selectPage(Page<Task> page, QueryWrapper<Task> queryWrapper) {
        List<Task> tasks = taskService.createTaskQuery().listPage((int) page.getCurrent(), (int) page.getSize());
        page.setRecords(tasks);
        return page;
    }

    @Override
    public Task get(String key) {
        return taskService
                .createTaskQuery()
                .taskId(key)
                .active()
                .singleResult();
    }

}
