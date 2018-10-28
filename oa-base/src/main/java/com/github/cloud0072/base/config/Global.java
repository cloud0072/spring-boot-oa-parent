package com.github.cloud0072.base.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 运行时设置
 *
 * @author cloud0072
 */
@Slf4j
@Component
public class Global {

    //  注册模块
    private final static Map<String, Object> moduleMap = new LinkedHashMap<>();
    //  配置
    //  网站主题样式
    //  是否允许指定IP访问
    //  设置 Application 级别的属性
    private final static Map<String, Serializable> configMap = new LinkedHashMap<>();

    public static void addModule(String name, Object module) {
        if (moduleMap.containsKey(name)) {
            throw new UnsupportedOperationException("已经注册该模块\t" + name);
        }
        moduleMap.put(name, module);
    }
    public static boolean containModule(String name) {
        return moduleMap.containsKey(name);
    }

    public static Serializable getConfig(String key) {
        return configMap.get(key);
    }

    public static void setConfig(String key, Serializable value) {
        if (configMap.containsKey(key)) {
            log.info("已经存在该配置\t: {}\t由 {} 修改为 {}", key, configMap.get(key), value);
        }
        configMap.put(key, value);
    }


}
