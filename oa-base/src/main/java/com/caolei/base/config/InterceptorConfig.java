package com.caolei.base.config;

import com.caolei.base.interceptor.DefaultInterceptor;
import com.caolei.base.interceptor.MenuInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final DefaultInterceptor defaultInterceptor;
    private final MenuInterceptor menuInterceptor;

    @Autowired
    public InterceptorConfig(DefaultInterceptor defaultInterceptor,
                             MenuInterceptor menuInterceptor) {
        this.defaultInterceptor = defaultInterceptor;
        this.menuInterceptor = menuInterceptor;
    }

    private static void excludeStaticResource(InterceptorRegistration registration) {
        registration
                .excludePathPatterns("/**/*.js")
                .excludePathPatterns("/**/*.css")
                .excludePathPatterns("/**/*.png")
                .excludePathPatterns("/**/*.gif")
                .excludePathPatterns("/**/*.jpg")
                .excludePathPatterns("/**/*.jpeg")
                .excludePathPatterns("/**/*.woff");
    }

    /**
     * 多个拦截器组成一个拦截器链
     * addPathPatterns 用于添加拦截规则
     * excludePathPatterns 用户排除拦截
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration ir1 = registry.addInterceptor(defaultInterceptor).addPathPatterns("/**");
        excludeStaticResource(ir1);

        InterceptorRegistration ir2 = registry.addInterceptor(menuInterceptor).addPathPatterns("/**");
        excludeStaticResource(ir2);

    }

}