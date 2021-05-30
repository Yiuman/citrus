package com.github.yiuman.citrus.support.file;

import cn.hutool.core.io.FileUtil;
import org.springframework.util.StreamUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 本地文件存储服务实现
 *
 * @author yiuman
 * @date 2021/3/21
 */
public class LocalFileStorageServiceImpl implements FileStorageService {

    private String path = "./temp";

    /**
     * 日期格式化
     */
    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public LocalFileStorageServiceImpl() {
    }

    public LocalFileStorageServiceImpl(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String save(String filepath, InputStream stream) throws IOException {
        //文件目录
        try {
            String directoryPath = path + "/" + LocalDate.now().format(FORMATTER);
            //创建目录
            FileUtil.mkdir(directoryPath);
            String filePath = directoryPath + "/" + filepath;
            FileOutputStream fileOutputStream = new FileOutputStream(FileUtil.file(filePath));
            StreamUtils.copy(stream, fileOutputStream);
            return filePath;
        } finally {
            stream.close();
        }

    }

    @Override
    public InputStream get(String filepath) throws IOException {
        return new FileInputStream(FileUtil.file(filepath));
    }
}
