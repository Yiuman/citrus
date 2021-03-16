package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.github.yiuman.citrus.system.commons.model.AbstractAuditingEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件资源对象
 *
 * @author yiuman
 * @date 2021/3/16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FileResource extends AbstractAuditingEntity {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String fileId;

    /**
     * 文件资源的MD5值，后续用来做断点续传
     */
    private String md5;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 二进制数据，与path必须有一个
     */
    private Byte[] bytes;

    /**
     * 存的路径
     */
    private String path;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 文件类型
     */
    private String fileType;

}
