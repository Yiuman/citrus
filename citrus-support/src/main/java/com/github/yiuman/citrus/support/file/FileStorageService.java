package com.github.yiuman.citrus.support.file;

import java.io.IOException;
import java.io.InputStream;

/**
 * 文件服务类
 *
 * @author yiuman
 * @date 2021/3/18
 */
public interface FileStorageService {

    /**
     * 上传文件
     *
     * @param filepath 文件路径
     * @param stream   输入流
     * @return 文件路径
     * @throws IOException IO异常
     */
    String save(String filepath, InputStream stream) throws IOException;


    /**
     * 获取文件资源
     *
     * @param filepath 文件ID
     * @return 文件流
     * @throws IOException IO异常
     */
    InputStream get(String filepath) throws IOException;

}