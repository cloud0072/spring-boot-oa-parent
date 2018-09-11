package com.caolei.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @ClassName: PluginProperties
 * @Description: 插件配置信息
 * @author caolei
 * @date 2018/9/10 11:29
 */

@Configuration
@Slf4j
public class PropertiesConfiguration implements Ordered {
    /**
     * shiro插件配置
     */
    @Bean
    @ConfigurationProperties(prefix = "plugin.shiro")
    public Shiro shiro() {
        return new Shiro();
    }

    /**
     * swagger2插件配置
     */
    @Bean
    @ConfigurationProperties(prefix = "plugin.swagger2")
    public Swagger2 swagger2() {
        return new Swagger2();
    }

    /**
     * swagger2插件配置
     */
    @Bean
    @ConfigurationProperties(prefix = "location.resource")
    public Location location() {
        return new Location();
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
