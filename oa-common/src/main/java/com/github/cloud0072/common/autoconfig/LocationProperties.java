package com.github.cloud0072.common.autoconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author caolei
 * @ClassName: LocationProperties
 * @Description: 本地目录路径配置   给注入的值一个正确的默认值 和 数据类型
 * @date 2018/9/10 11:14
 */
@ConfigurationProperties(prefix = "location.resource")
@Component
@Getter
@Setter
public class LocationProperties {
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
}
