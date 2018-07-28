package com.caolei.system.config;

import com.caolei.system.interceptor.DefaultInterceptor;
import com.caolei.system.interceptor.MenuInterceptor;
import com.caolei.system.interceptor.VisitLogInterceptor;
import org.omg.PortableInterceptor.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
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

    private final VisitLogInterceptor visitLogInterceptor;
    private final DefaultInterceptor defaultInterceptor;
    private final MenuInterceptor menuInterceptor;

    @Autowired
    public InterceptorConfig(DefaultInterceptor defaultInterceptor,
                             VisitLogInterceptor visitLogInterceptor,
                             MenuInterceptor menuInterceptor) {
        this.defaultInterceptor = defaultInterceptor;
        this.visitLogInterceptor = visitLogInterceptor;
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

        InterceptorRegistration ir3 = registry.addInterceptor(visitLogInterceptor).addPathPatterns("/**");
        excludeStaticResource(ir3);

    }

}