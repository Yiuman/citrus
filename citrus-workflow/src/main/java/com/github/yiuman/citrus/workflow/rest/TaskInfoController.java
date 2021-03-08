package com.github.yiuman.citrus.workflow.rest;

import com.github.yiuman.citrus.support.crud.view.impl.PageTableView;
import lombok.Data;
import org.activiti.engine.task.Task;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务查询控制器
 *
 * @author yiuman
 * @date 2021/3/8
 */
@RestController
@RequestMapping("/rest/taskinst")
public class TaskInfoController extends BaseWorkflowQueryController<Task, String> {

    public TaskInfoController() {
        setParamClass(TaskQueryParams.class);
    }

    @Data
    static class TaskQueryParams {
        String taskCandidateOrAssigned;
    }

    @Override
    protected Object createView() {
        PageTableView<Task> view = new PageTableView<>();
        view.addWidget("处理人或候选人", "taskCandidateOrAssigned");

        view.addHeader("任务名称", "name");
        view.addHeader("处理人", "assignee");
        view.addHeader("创建时间", "createTime");
        view.addHeader("处理时间", "dueDate");
        return view;
    }


//    static class TaskInfo extends TaskEntityImpl {
//
//        @Override
//        public Map<String, VariableInstance> getVariableInstances() {
//            return null;
//        }
//
//        @Override
//        public ExecutionEntity getExecution() {
//            return null;
//        }
//
//        @Override
//        public ExecutionEntity getProcessInstance() {
//            return null;
//        }
//
//        @Override
//        protected VariableScopeImpl getParentVariableScope() {
//            return null;
//        }
//    }

//    @Override
//    protected Function<Task, ? extends Task> getTransformFunc() {
//        return item -> {
//            TaskInfo taskInfo = new TaskInfo();
//            BeanUtils.copyProperties(item, taskInfo, "execution","processInstance","parentVariableScope");
//            return taskInfo;
//        };
//    }
}
