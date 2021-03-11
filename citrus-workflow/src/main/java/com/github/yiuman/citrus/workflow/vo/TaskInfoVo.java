package com.github.yiuman.citrus.workflow.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.activiti.engine.task.Task;

import java.util.Map;

/**
 * @author yiuman
 * @date 2021/3/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TaskInfoVo extends BaseTaskVo implements Task {

    @Override
    public Map<String, Object> getTaskLocalVariables() {
        return null;
    }

    @Override
    public Map<String, Object> getProcessVariables() {
        return null;
    }

}
