package com.github.yiuman.citrus.workflow;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.yiuman.citrus.workflow.model.ProcessBusinessModel;
import lombok.Data;

/**
 * @author yiuman
 * @date 2020/12/21
 */
@Data
@TableName("sys_leave")
public class Leave implements ProcessBusinessModel {

    @TableId
    private Long leaveId;

    private Integer leaveDay;

    private Long userId;

    private String processInstanceId;

    private Integer state;

}
