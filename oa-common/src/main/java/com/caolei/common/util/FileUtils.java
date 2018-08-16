package com.caolei.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 处理时间的工具类
 *
 * @author cloud0072
 */
@Component
public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    //文件保存路径
    private static String uploadPath;
    private static File uploadDir;

    private FileUtils() {
    }

    /**
     * 判断是否是Linux操作系统
     *
     * @return
     */
    public static Boolean isLinuxSystem() {
        return File.separator.equals("/");
    }

    /**
     * 判断是否是Windows操作系统
     *
     * @return
     */
    public static Boolean isWindowsSystem() {
        return File.separator.equals("\\");
    }

    /**
     * 获取文件上传路径
     *
     * @return
     */
    public static String uploadPath() {
        if (StringUtils.isEmpty(uploadPath)) {
            throw new UnsupportedOperationException("文件上传路径未定义!");
        }
        return uploadPath;
    }

    /**
     * 初始化文件上传文件夹
     *
     * @param uploadPath
     */
    @Value("${file.upload.path}")
    private void setUploadPath(String uploadPath) {
        FileUtils.uploadPath = uploadPath;

        if (uploadDir == null) {
            uploadDir = new File(uploadPath());
            if (!uploadDir.exists() && !uploadDir.mkdirs() || uploadDir.isFile()) {
                throw new UnsupportedOperationException("无法创建文件上传文件夹");
            }
        }
    }

}
