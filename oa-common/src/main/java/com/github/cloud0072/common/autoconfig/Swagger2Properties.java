package com.github.cloud0072.common.autoconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author caolei
 * @ClassName: Swagger2Properties
 * @Description: swagger2插件配置
 * @date 2018/9/11 21:28
 */
@ConfigurationProperties(prefix = "plugin.swagger2")
@Component
@Getter
@Setter
public class Swagger2Properties {
    // 是否显示文档
    private Boolean enable = false;

}
