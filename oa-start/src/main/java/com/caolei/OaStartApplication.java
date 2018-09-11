package com.caolei;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动方法的入口
 * 一定要在所有包的上级，不然无法扫描到注解
 *
 * @author cloud0072
 */
@Slf4j
@SpringBootApplication
public class OaStartApplication {

    private static String port;
    private static String context_path;

    public static void main(String[] args) {
        SpringApplication.run(OaStartApplication.class, args);

        String protocol = "http://";
        String host = "localhost";
        log.info(protocol + host + ":" + port + context_path + "");
    }

    @Value("${server.port}")
    public void setPort(String port) {
        OaStartApplication.port = port;
    }

    @Value("${server.servlet.context-path}")
    public void setContext_path(String context_path) {
        OaStartApplication.context_path = context_path;
    }

}
