package com.caolei.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;


/**
 * 处理时间的工具类
 *
 * @author cloud0072
 */
@Slf4j
@Component
public class FileUtils {
//
//    @Autowired
//    private Location location;

    //项目基础路径
    private static String basePath;
    //文件保存路径
    private static String uploadPath;
    //日志文件目录
    private static String logPath;
    //日志文件目录
    private static String logFile;

    private FileUtils() {
    }

    /**
     * 判断是否是Linux操作系统
     *
     * @return
     */
    public static Boolean isLinux() {
        return File.separator.equals("/");
    }

    /**
     * 判断是否是Windows操作系统
     *
     * @return
     */
    public static Boolean isWindows() {
        return File.separator.equals("\\");
    }

    /**
     * 初始化目录
     * @param filePath
     */
    public static void initDirectory(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return;
        }
        try {
            File file = new File(filePath);
            if (!file.exists() || file.isFile()) {
                if (file.mkdirs()) {
                    throw new UnsupportedOperationException("无法初始化目录:\t" + filePath);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void checkDirectory(String path) {
        if (!StringUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists() && file.isDirectory()) {
                return;
            }
        }
        throw new UnsupportedOperationException("目录不可用 : \t" + path);
    }

    public static void checkFile(String path) {
        if (!StringUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                return;
            }
        }
        throw new UnsupportedOperationException("文件不可用 : \t" + path);
    }

    /******************************************************************************************************************/

    public static String getUploadPath() {
        checkDirectory(uploadPath);
        return uploadPath;
    }

    @Value("${location.resource.upload-path}")
    private void setUploadPath(String uploadPath) {
        FileUtils.uploadPath = uploadPath;
        initDirectory(uploadPath);
    }

    public static String getLogPath() {
        checkDirectory(logPath);
        return logPath;
    }

    @Value("${location.resource.log-path}")
    private void setLogPath(String logPath) {
        FileUtils.logPath = logPath;
        initDirectory(logPath);
    }

    public static String getLogFile() {
        checkFile(logFile);
        return logFile;
    }

    @Value("${logging.file}")
    public void setLogFile(String logFile) {
        FileUtils.logFile = logFile;
    }

    public static String getBasePath() {
        checkDirectory(basePath);
        return basePath;
    }

    @Value("${location.resource.base-path}")
    public void setBasePath(String basePath) {
        FileUtils.basePath = basePath;
        initDirectory(basePath);
    }

}
