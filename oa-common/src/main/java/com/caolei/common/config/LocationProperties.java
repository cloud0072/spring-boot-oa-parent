package com.caolei.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName: LocationProperties
 * @Description: 给注入的值一个正确的默认值 和 数据类型
 * @author caolei
 * @date 2018/9/10 11:14
 */
@Component
@ConfigurationProperties(prefix = "location.resource")
public class LocationProperties {
    // 外部文件基础路径
    private String path;
    // 静态文件读取的路径
    private String staticPath;
    // 上传文件存放路径
    private String uploadPath;
    // 配置文件存放路径
    private String configPath;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

}
