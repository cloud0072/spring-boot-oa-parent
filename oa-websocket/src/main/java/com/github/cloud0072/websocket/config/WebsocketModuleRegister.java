package com.github.cloud0072.websocket.config;

import com.github.cloud0072.base.config.Global;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WebsocketModuleRegister
        implements ApplicationRunner, Ordered {


    @Override
    public void run(ApplicationArguments args) throws Exception {

        Global.addModule("websocket", "websocket");

    }

    @Override
    public int getOrder() {
        return 1;
    }

}
