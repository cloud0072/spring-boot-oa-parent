package com.caolei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动方法的入口
 * 一定要在所有包的上级，不然无法扫描到注解
 * @author cloud0072
 */
@SpringBootApplication
public class OaStartApplication {

	public static void main(String[] args) {
		SpringApplication.run(OaStartApplication.class, args);
	}
}
