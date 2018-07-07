package com.caolei.system.interceptor;

import com.caolei.system.api.BaseLogger;
import com.caolei.system.utils.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MenuInterceptor implements HandlerInterceptor, BaseLogger {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        String currentMenuId = request.getParameter("menuId");
        if (!StringUtils.isEmpty(currentMenuId)) {
            request.getSession().setAttribute("menuId", currentMenuId);
        }
    }
}
