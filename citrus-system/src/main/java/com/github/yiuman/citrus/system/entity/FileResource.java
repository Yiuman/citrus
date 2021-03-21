package com.github.yiuman.citrus.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("sys_file")
public class FileResource extends AbstractAuditingEntity {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String fileId;

    /**
     * 文件资源的MD5值，后续用来做断点续传
     */
    private String identify;

    /**
     * 文件的编号，若是使用分片上传，这里记录的就是分片的下标
     */
    private Integer fileNo;

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
