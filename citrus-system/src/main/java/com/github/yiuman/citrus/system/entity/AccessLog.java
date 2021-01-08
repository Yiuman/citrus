package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 准入日志
 * 用于记录接口的访问
 *
 * @author yiuman
 * @date 2020/9/22
 */
@Data
@TableName("sys_access_log")
public class AccessLog {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户ID，匿名为空
     */
    private Long userId;

    /**
     * 用户名，匿名为'anonymous'
     */
    private String username;

    /**
     * 对方的IP地址
     */
    private String ipAddress;

    /**
     * 请求的地址
     */
    private String url;

    /**
     * 请求方式、方法
     */
    private String requestMethod;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 资源ID
     */
    private Long resourceId;

    /**
     * 访问的资源名称
     */
    private String resourceName;

    /**
     * 资源类型
     * -1 未定义的资源
     * 0 菜单
     * 2 操作
     */
    private Integer resourceType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
