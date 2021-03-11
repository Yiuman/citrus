package com.github.yiuman.citrus.workflow.vo;

import lombok.Data;
import org.activiti.engine.task.DelegationState;

import java.util.Date;

import static org.activiti.engine.task.Task.DEFAULT_PRIORITY;

/**
 * @author yiuman
 * @date 2021/3/11
 */
@Data
public class BaseTaskVo {

    private String id;
    private String owner;
    private int assigneeUpdatedCount; // needed for v5 compatibility
    private String originalAssignee; // needed for v5 compatibility
    private String assignee;
    private DelegationState delegationState;
    private String parentTaskId;
    private String name;
    private String localizedName;
    private String description;
    private String localizedDescription;
    private int priority = DEFAULT_PRIORITY;
    private Date createTime; // The time when the task has been created
    private Date dueDate;
    private int suspensionState;
    private String category;
    private boolean isIdentityLinksInitialized;
    private String executionId;
    private String processInstanceId;
    private String processDefinitionId;
    private String taskDefinitionKey;
    private String formKey;
    private boolean isDeleted;
    private boolean isCanceled;
    private String eventName;
    private String tenantId;
    private boolean forcedUpdate;
    private boolean suspended;
    private Date claimTime;
    private Integer appVersion;
    private String businessKey;

}
