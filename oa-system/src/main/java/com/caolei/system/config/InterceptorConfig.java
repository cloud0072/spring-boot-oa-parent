package com.caolei.system.config;

import com.caolei.system.interceptor.DefaultInterceptor;
import com.caolei.system.interceptor.MenuInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器在此注册
 *
 * @author cloud0072
 */
@Configuration
public class InterceptorConfig
        implements WebMvcConfigurer {

    /**
     * 多个拦截器组成一个拦截器链
     * addPathPatterns 用于添加拦截规则
     * excludePathPatterns 用户排除拦截
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration ir1 = registry.addInterceptor(new DefaultInterceptor());
        ir1.addPathPatterns("/**");
        ir1.excludePathPatterns("/**/*.js");
        ir1.excludePathPatterns("/**/*.css");
        ir1.excludePathPatterns("/**/*.png");
        ir1.excludePathPatterns("/**/*.gif");
        ir1.excludePathPatterns("/**/*.jpg");
        ir1.excludePathPatterns("/**/*.jpeg");
        ir1.excludePathPatterns("/**/*.woff");

        InterceptorRegistration ir2 = registry.addInterceptor(new MenuInterceptor());
        ir2.addPathPatterns("/**");
        ir2.excludePathPatterns("/**/*.js");
        ir2.excludePathPatterns("/**/*.css");
        ir2.excludePathPatterns("/**/*.png");
        ir2.excludePathPatterns("/**/*.gif");
        ir2.excludePathPatterns("/**/*.jpg");
        ir2.excludePathPatterns("/**/*.jpeg");
        ir2.excludePathPatterns("/**/*.woff");
    }

}