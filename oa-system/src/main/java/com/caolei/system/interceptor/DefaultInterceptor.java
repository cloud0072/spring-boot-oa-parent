package com.caolei.system.interceptor;

import com.caolei.system.api.BaseLogger;
import com.caolei.system.utils.RequestUtils;
import com.caolei.system.utils.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * 默认拦截器
 *
 * @author cloud0072
 * @date 2018/6/12 22:41
 */
@Component
public class DefaultInterceptor implements HandlerInterceptor, BaseLogger {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }
}
