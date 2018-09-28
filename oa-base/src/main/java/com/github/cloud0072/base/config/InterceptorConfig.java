package com.github.cloud0072.base.config;

import com.github.cloud0072.base.interceptor.BaseInterceptor;
import com.github.cloud0072.base.interceptor.ModelAndViewInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * 拦截器在此注册
 *
 * @author cloud0072
 */
@Configuration
public class InterceptorConfig
        implements WebMvcConfigurer {

    @Autowired
    private BaseInterceptor baseInterceptor;
    @Autowired
    private ModelAndViewInterceptor modelAndViewInterceptor;

    /**
     * 多个拦截器组成一个拦截器链
     * addPathPatterns 用于添加拦截规则
     * excludePathPatterns 用户排除拦截
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludePatterns = new ArrayList<>();
        excludePatterns.add("/assets/**");

        registry.addInterceptor(baseInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePatterns);

        registry.addInterceptor(modelAndViewInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePatterns);

    }

}