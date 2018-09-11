package com.caolei.common.config;

/**
 * @ClassName: LocationProperties
 * @Description: 给注入的值一个正确的默认值 和 数据类型
 * @author caolei
 * @date 2018/9/10 11:14
 */
public class Location {
    // 外部文件基础路径
    private String basePath;
    // 静态文件读取的路径
    private String staticPath;
    // 上传文件存放路径
    private String uploadPath;
    // 配置文件存放路径
    private String configPath;
    // 日志文件目录
    private String logPath;
    // 数据库文件目录  -- 目前 仅用于h2 数据库
    private String dbPath;

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getStaticPath() {
        return staticPath;
    }

    public void setStaticPath(String staticPath) {
        this.staticPath = staticPath;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getDbPath() {
        return dbPath;
    }

    public void setDbPath(String dbPath) {
        this.dbPath = dbPath;
    }
}
