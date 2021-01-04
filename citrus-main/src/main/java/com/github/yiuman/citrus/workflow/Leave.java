package com.github.yiuman.citrus.workflow;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.yiuman.citrus.workflow.model.ProcessBusinessModel;
import lombok.Data;

/**
 * -- ------------请假表----------------
 * DROP TABLE IF EXISTS `sys_leave`;
 * CREATE TABLE `sys_leave`
 * (
 * `leave_id`            bigint(20) NOT NULL COMMENT '主键id',
 * `leave_day`           int(3) DEFAULT NULL COMMENT '请假天数',
 * `user_id`           bigint(20) DEFAULT NULL COMMENT '用户ID',
 * `process_instance_id`           varchar(500) DEFAULT NULL COMMENT '流程实例主键',
 * `state`              int(2) DEFAULT NULL COMMENT '状态',
 * PRIMARY KEY (`leave_id`) USING BTREE
 * ) ENGINE = InnoDB
 * DEFAULT CHARSET = utf8
 * ROW_FORMAT = DYNAMIC COMMENT ='请假表';
 *
 * @author yiuman
 * @date 2020/12/21
 */
@Data
@TableName("sys_leave")
public class Leave implements ProcessBusinessModel {

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId
    private Long leaveId;

    private Integer leaveDay;

    private Long userId;

    private String processInstanceId;

    private Integer state;

}
