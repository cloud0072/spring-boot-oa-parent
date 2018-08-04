package com.caolei.system.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * http状态码 跳转配置
 */
@Configuration
public class ErrorPageConfig implements ErrorPageRegistrar {

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        //1、按错误的类型显示错误的网页
        //错误类型为401，认证信息错误，返回登录页
        ErrorPage e401 = new ErrorPage(HttpStatus.UNAUTHORIZED, "/prepare_login");
        //错误类型为404，找不到网页的，默认显示404.html网页
        ErrorPage e404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error/404");

        registry.addErrorPages(e401,e404);
    }
}
