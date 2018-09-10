package com.caolei.base.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName: LocationProperties
 * @Description: 给注入的值一个正确的默认值
 * @author caolei
 * @date 2018/9/10 11:14
 */
@Component
@ConfigurationProperties(prefix = "location.resource")
public class LocationProperties {

    private String path;
    private String staticPath;
    private String uploadPath;
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
