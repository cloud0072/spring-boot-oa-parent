package com.caolei.base.config;

import com.caolei.base.interceptor.DefaultInterceptor;
import com.caolei.base.interceptor.MenuInterceptor;
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
    private DefaultInterceptor defaultInterceptor;
    @Autowired
    private MenuInterceptor menuInterceptor;

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
        excludePatterns.add("/**/*.js");
        excludePatterns.add("/**/*.css");
        excludePatterns.add("/**/*.png");
        excludePatterns.add("/**/*.gif");
        excludePatterns.add("/**/*.jpg");
        excludePatterns.add("/**/*.jpeg");
        excludePatterns.add("/**/*.woff");

        registry.addInterceptor(defaultInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePatterns);

        registry.addInterceptor(menuInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePatterns);

//        registry.addInterceptor(urlPathMatchingInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns(excludePatterns);
    }

}