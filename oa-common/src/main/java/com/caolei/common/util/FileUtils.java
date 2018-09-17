package com.caolei.common.util;

import com.caolei.common.autoconfig.LocationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    //本地文件配置
    private static LocationProperties location;
    //日志文件目录
    private static String logFile;

    @Autowired
    private FileUtils(LocationProperties location) {
        FileUtils.location = location;

        initDirectory(location.getBasePath());
        initDirectory(location.getConfigPath());
        initDirectory(location.getDbPath());
        initDirectory(location.getLogPath());
        initDirectory(location.getStaticPath());
        initDirectory(location.getUploadPath());
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
                if (!file.mkdirs()) {
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

    public static String getLogFile() {
        checkFile(logFile);
        return logFile;
    }

    @Value("${logging.file}")
    public void setLogFile(String logFile) {
        FileUtils.logFile = logFile;
    }

    public static String getUploadPath() {
        checkDirectory(location.getUploadPath());
        return location.getUploadPath();
    }

    public static String getConfigPath() {
        checkDirectory(location.getConfigPath());
        return location.getConfigPath();
    }

    public static String getStaticPath() {
        checkDirectory(location.getStaticPath());
        return location.getStaticPath();
    }

    public static String getBasePath() {
        checkDirectory(location.getBasePath());
        return location.getBasePath();
    }



}
