package com.caolei.system.installer;

import com.caolei.common.api.BaseLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 启动完成后
 */
@Component
public class StartupFinished
        implements ApplicationRunner, Ordered, BaseLogger {

    @Autowired
    private Environment env;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String protocol = "http://";
        String host = "localhost";
        String port = env.getProperty("server.port");
        String context_path = env.getProperty("server.servlet.context-path");
        logger().info(protocol + host + ":" + port + context_path);
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
