package com.caolei;

import com.caolei.common.util.FileUtils;
import com.caolei.common.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
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

    public static void main(String[] args) {
        SpringApplication.run(OaStartApplication.class, args);

        log.info("SERVER BASE\t{}/", FileUtils.getBasePath());
        log.info("SERVER URL \thttp://localhost:" + HttpUtils.getPort() + HttpUtils.getContext_path() + "\n");

    }

}
