package com.github.yiuman.citrus.workflow.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.activiti.engine.history.HistoricTaskInstance;

import java.util.Date;
import java.util.Map;

/**
 * 历史任务实例
 *
 * @author yiuman
 * @date 2021/3/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HistoricTaskVo extends BaseTaskVo implements HistoricTaskInstance {

    private String deleteReason;

    private Date startTime;

    private Date endTime;

    private Long durationInMillis;

    private Long workTimeInMillis;

    private Date time;

    @Override
    public Map<String, Object> getTaskLocalVariables() {
        return null;
    }

    @Override
    public Map<String, Object> getProcessVariables() {
        return null;
    }
}
