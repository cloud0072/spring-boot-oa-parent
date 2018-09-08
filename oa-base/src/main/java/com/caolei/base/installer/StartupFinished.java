package com.caolei.base.installer;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 启动完成后
 */
@Slf4j
@Component
public class StartupFinished
        implements ApplicationRunner, Ordered {

    @Value("${server.port}")
    private String port;
    @Value("${server.servlet.context-path}")
    private String context_path;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String protocol = "http://";
        String host = "localhost";
        log.info(protocol + host + ":" + port + context_path + "");
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
