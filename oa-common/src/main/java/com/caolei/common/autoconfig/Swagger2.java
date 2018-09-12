package com.caolei.common.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName: Swagger
 * @Description: swagger2插件配置
 * @author caolei
 * @date 2018/9/11 21:28
 */
@ConfigurationProperties(prefix = "plugin.swagger2")
@Component
public class Swagger2 {
    // 是否显示文档
    private boolean show = false;

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}
